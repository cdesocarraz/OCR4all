<%@ tag description="Training settings" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="settingsType" required="true" %>
<c:choose>
    <%-- General settings --%>
    <c:when test="${settingsType == 'general'}">
        <table class="compact">
            <tbody>
                <tr>
                    <td><p>Name of the model</p></td>
                    <td>
                        <div class="input-field">
                            <input id="--best_model_label" type="text" />
                            <label for="--best_model_label"></label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td><p>The number of fold, that is the number of models to train</p></td>
                    <td>
                        <div class="input-field">
                            <input id="--n_folds" type="number" />
                            <label for="--n_folds" data-type="int" data-error="Has to be integer">Default: 5 (Integer value)</label>
                            <%-- Required information as fallback in JS code (!!!IMPORTANT!!! change this value if the default changes !!!IMPORTANT!!!) --%>
                            <input id="defaultFolds" type="hidden" value="5" />
                        </div>
                    </td>
                </tr>
                <tr>
                    <td><p>Hardware type</p></td>
                    <td>
                        <div class="input-field">
                            <i class="material-icons prefix">perm_data_setting</i>
                            <select id="hardwareType" name="hardwareType" class="suffix ignoreParam">
                                <option value="CPU">CPU</option>
                                <option value="GPU">GPU</option>
                            </select>
                            <label></label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td><p>Number of models to train in parallel</p></td>
                    <td>
                        <div class="input-field">
                            <input id="--max_parallel_models" type="number" />
                            <label for="--max_parallel_models" data-type="int" data-error="Has to be integer">Default: -1 (Integer value) | Train all models in parallel</label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p>
                            Early stopping
                            <br />
                            <span class="userInfo">The number of models that must be worse than the current best model to stop</span>
                        </p>
                    </td>
                    <td>
                        <div class="input-field">
                            <input id="--early_stopping_nbest" type="number" />
                            <label for="--early_stopping_nbest" data-type="int" data-error="Has to be integer">Default: 10 (Integer value)</label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p>
                            The number of iterations for training
                            <br />
                            <span class="userInfo">If using early stopping, this is the maximum number of iterations</span>
                        </p>
                    </td>
                    <td>
                        <div class="input-field">
                            <input id="--max_iters" type="number" />
                            <label for="--max_iters" data-type="int" data-error="Has to be integer">Default: 1000000 (Integer value)</label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td><p>Pre-Training</p></td>
                    <td>
                        <div class="input-field">
                            <i class="material-icons prefix">queue_play_next</i>
                            <select id="pretrainingType" name="pretrainingType" class="suffix ignoreParam">
                                <option value="from_scratch">Train all models from scratch</option>
                                <option value="single_model">Train all models based on one existing model</option>
                                <option value="multiple_models">Train each model based on different existing models</option>
                            </select>
                            <label></label>
                        </div>
                    </td>
                </tr>
                <%-- START Will be used to generate dropdown elements for each model --%>
                <tr id="pretrainingDummyTr" style="display: none;" data-id="pretrainingTr">
                    <td><p>Pre-Training for model: <span></span></p></td>
                    <td>
                        <div class="input-field">
                            <select id="pretrainingDummySelect" name="pretrainingDummySelect" data-id="pretrainingModelSelect" class="ignoreParam">
                                <option value="None">Train model from scratch</option>
                            </select>
                            <label></label>
                        </div>
                    </td>
                </tr>
                <%-- END Will be used to generate dropdown elements for each model --%>
            </tbody>
        </table>
    </c:when>
    <%-- Advanced settings --%>
    <c:when test="${settingsType == 'advanced'}">
        <table class="compact">
            <tbody>
            </tbody>
        </table>
    </c:when>
</c:choose>