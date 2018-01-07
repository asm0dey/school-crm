<#-- @ftlvariable name="degrees" type="java.util.List<kotlin.Pair<org.ort.school.app.repo.GradeInfoDTO, java.util.List<org.ort.school.app.repo.ParentWithChildren>>>" -->
<#-- @ftlvariable name="allowedLetters" type="java.util.Collection<java.lang.String>" -->
<#-- @ftlvariable name="allowedDegrees" type="java.util.Collection<java.lang.Integer>" -->
  <div class="fixed-action-btn">
      <a class="btn-floating btn-large waves-effect waves-light red modal-trigger" href="#create-degree"><i
              class="material-icons">add</i></a>
  </div>

  <div id="create-degree" class="modal modal-fixed-footer">
      <div class="modal-content">
          <h4>Добавление класса</h4>
          <form action="/private/degree/create" class="col s12" method="post">
              <div class="row">
                  <div class="input-field  col s12">
                      <select name="degreeNo" id="degreeNo" class="validate" required>
                          <option value="no" selected disabled>Выберите номер класса</option>
                            <#list allowedDegrees as degree>
                                <option value="${degree}">${degree}</option>
                            </#list>
                      </select>
                      <label for="degreeNo">Номер класса</label>
                  </div>
              </div>
              <div class="row">
                  <div class="input-field  col s12">
                      <select name="degreeLetter" id="degreeLetter" class="validate" required>
                          <option value="no" selected disabled>Выберите букву класса</option>
                            <#list allowedLetters as letter>
                                <option value="${letter}">${letter}</option>
                            </#list>
                      </select>
                      <label for="degreeLetter">Буква класса</label>
                  </div>
              </div>
              <div class="row">
                  <div class="col s4 valign-wrapper input-field">
                      <button class="btn waves-effect waves-light" type="submit">Создать
                          <i class="material-icons right">send</i>
                      </button>
                  </div>
              </div>
          </form>
      </div>
  <#--<div class="modal-footer">
      <button class="btn waves-effect waves-light" type="submit">Создать
          <i class="material-icons right">send</i>
      </button>
  </div>-->
  </div>
    <div class="row">
        <ul class="collapsible " data-collapsible="accordion">
            <#list degrees as degree>
                <li>
                    <div class="collapsible-header">${degree.first.fullGradeName}</div>
                    <div class="collapsible-body">
                        <table class="bordered responsive-table">
                            <thead>
                            <tr>
                                <th>Родитель</th>
                                <th>Ребёнок</th>
                            </tr>
                            </thead>

                            <tbody>
                            <#list degree.second as parentWithChildren>
                                <#if parentWithChildren.parent??>
                                    <#list parentWithChildren.children as child>
                                        <tr>
                                            <#if child?index==0>
                                            <td rowspan="${parentWithChildren.children?size}">${parentWithChildren.parent.displayName}</td>
                                            </#if>
                                            <td>${child.getDisplayName()}</td>
                                        </tr>
                                    </#list>
                                </#if>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </li>
            </#list>
        </ul>
    </div>
<#--<div class="row">
    <h3>Добавление класса</h3>
    <div class="row">
        <form action="/private/degree/create" class="col s12" method="post">
            <div class="row">
                <div class="input-field inline col s4">
                    <select name="degreeNo" id="degreeNo" class="validate" required>
                        <option value="no" selected disabled>Выберите номер класса</option>
                        <#list allowedDegrees as degree>
                            <option value="${degree}">${degree}</option>
                        </#list>
                    </select>
                    <label for="degreeNo">Номер класса</label>
                </div>
                <div class="input-field inline col s4">
                    <select name="degreeLetter" id="degreeLetter" class="validate" required>
                        <option value="no" selected disabled>Выберите букву класса</option>
                        <#list allowedLetters as letter>
                            <option value="${letter}">${letter}</option>
                        </#list>
                    </select>
                    <label for="degreeLetter">Буква класса</label>
                </div>
                <div class="col s4 valign-wrapper input-field">
                    <button class="btn waves-effect waves-light" type="submit">Создать
                        <i class="material-icons right">send</i>
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>-->
