package org.ort.school.app.job

import com.google.inject.Inject
import org.jooby.quartz.Scheduled
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.ort.school.app.repo.DegreeRepo
import org.quartz.JobExecutionContext

class GradeUpdater @Inject constructor(
        private val degreeRepo: DegreeRepo,
        private val ctx: DSLContext
) {
    @Scheduled("0 32 3 L AUG ? *")
    fun execute(p0: JobExecutionContext?) {
        ctx.transactionResult { conf ->
            val tx = DSL.using(conf)
            degreeRepo.moveDegreesForward(tx)
            degreeRepo.deleteTooOldDegrees(tx)
        }
    }

}
