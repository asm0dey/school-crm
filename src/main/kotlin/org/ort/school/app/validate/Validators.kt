package org.ort.school.app.validate

import com.google.inject.Inject
import org.ort.school.app.repo.UserRepo
import org.ort.school.app.routes.UserInfoDTO
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(FIELD, VALUE_PARAMETER, PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueUsernameValidator::class])
@MustBeDocumented
annotation class UniqueUsername(
        val message: String = "Username should be unique",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class UniqueUsernameValidator @Inject constructor(val userRepo: UserRepo) : ConstraintValidator<UniqueUsername, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value == null || userRepo.countUsersBy(value) == 0L
    }

    override fun initialize(constraintAnnotation: UniqueUsername?) {
    }
}