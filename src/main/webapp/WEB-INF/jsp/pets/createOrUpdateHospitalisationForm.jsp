<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>


<petclinic:layout pageName="hospitalisations">
	<jsp:attribute name="customScript">
        <script>
									$(function() {
										$("#startDate").datepicker({
											dateFormat : 'yy/mm/dd'
										});
									});
								</script>
        <script>
									$(function() {
										$("#finishDate").datepicker({
											dateFormat : 'yy/mm/dd'
										});
									});
								</script>
    </jsp:attribute>
	<jsp:body>
        <h2>
			<c:if test="${hospitalisation['new']}">New </c:if>Hospitalisation</h2>
        
         <b>Pet</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th>Birth Date</th>
                <th>Type</th>
                <th>Owner</th>
            </tr>
            </thead>
            <tr>
            	<td><c:out value="${pet.name}" /></td>
                <td><petclinic:localDate
						date="${pet.birthDate}" pattern="yyyy/MM/dd" /></td>
                <td><c:out value="${pet.type.name}" /></td>
                <td><c:out
						value="${pet.owner.firstName} ${pet.owner.lastName}" /></td>
            </tr>
        </table>
        
        <form:form modelAttribute="hospitalisation"	class="form-horizontal">
        	<div class="form-group has-feedback">
       			<petclinic:inputField label="Treatment" name="treatment" />
       			<petclinic:inputField label="Diagnosis" name="diagnosis" />
       			<petclinic:inputField label="Total price" name="totalPrice" />
       			<c:if test="${pet.status == 'SICK'}">
       			<petclinic:selectField label="Hospitalisation status" name="hospitalisationStatus" 
       			names="${hospitalisation_status}" size="2"/>
       			</c:if>
       			
        	</div>
        	
        	<div class="form-group">
            	<div class="col-sm-offset-2 col-sm-10">
               		<button class="btn btn-default" type="submit">Save changes</button>
            	</div>
        	</div>
   		</form:form>
   		
   		 <br />
        <b>Previous Hospitalisations</b>
        <table class="table table-striped">
            <tr>
                <th>Start Date</th>
                <th>Finish Date</th>
                <th>Diagnosis</th>
            </tr>
            <c:forEach var="hospitalisation" items="${pet.hospitalisations}">
                <c:if test="${!hospitalisation['new']}">
                    <tr>
                       <td><petclinic:localDate
								date="${hospitalisation.startDate}" pattern="yyyy/MM/dd" /></td>
                       <td><petclinic:localDate
								date="${hospitalisation.finishDate}" pattern="yyyy/MM/dd" /></td>
					   <td><c:out value="${hospitalisation.diagnosis}" /></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
	</jsp:body>
</petclinic:layout>
