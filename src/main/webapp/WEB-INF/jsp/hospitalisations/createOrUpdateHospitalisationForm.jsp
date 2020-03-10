<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="owners">
        <h2>
        	<c:if test="${hospitalisation['new']}">New </c:if>Hospitalisation
       	</h2>
        <form:form modelAttribute="hospitalisation" class="form-horizontal" id="add-hospitalisation-form">
        <div class="form-group has-feedback">
       		<petclinic:inputField label="Name" name="name"/>
       		<petclinic:inputField label="Start date" name="start_date"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
               <button class="btn btn-default" type="submit">Add Hospitalisation</button>
            </div>
        </div>
    </form:form>
</petclinic:layout>
