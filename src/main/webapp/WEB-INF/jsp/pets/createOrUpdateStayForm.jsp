<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>


<petclinic:layout pageName="stays">
	<jsp:attribute name="customScript">
        <script>
									$(function() {
										$("#startdate").datepicker({
											dateFormat : 'yy/mm/dd'
										});
									});
								</script>
        <script>
									$(function() {
										$("#finishdate").datepicker({
											dateFormat : 'yy/mm/dd'
										});
									});
								</script>
    </jsp:attribute>
	<jsp:body>
        <h2>
			<c:if test="${stay['new']}">New </c:if>Stay</h2>

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
                <td><c:out value="${stay.pet.name}" /></td>
                <td><petclinic:localDate
						date="${stay.pet.birthDate}" pattern="yyyy/MM/dd" /></td>
                <td><c:out value="${stay.pet.type.name}" /></td>
                <td><c:out
						value="${stay.pet.owner.firstName} ${stay.pet.owner.lastName}" /></td>
            </tr>
        </table>

        <form:form modelAttribute="stay" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Start Date"
					name="startdate" />
                <petclinic:inputField label="Finish Date"
					name="finishdate" />
                 <petclinic:inputField label="Special Cares"
					name="specialCares" />
                  <petclinic:inputField label="Price" name="price" />
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="petId"
						value="${stay.pet.id}" />
                    <button class="btn btn-default" type="submit">Add Stay</button>
                </div>
            </div>
        </form:form>

        <br />
        <b>Previous Hotels</b>
        <table class="table table-striped">
            <tr>
                <th>Start Date</th>
                <th>Finish Date</th>
                <th>Special Cares</th>
                <th>Price</th>
            </tr>
            <c:forEach var="stay" items="${stay.pet.stays}">
                <c:if test="${!stay['new']}">
                    <tr>
                       <td><petclinic:localDate
								date="${stay.startdate}" pattern="yyyy/MM/dd" /></td>
                        <td><petclinic:localDate
								date="${stay.finishdate}" pattern="yyyy/MM/dd" /></td>
                        <td><c:out value="${stay.specialCares}" /></td>
                        <td><c:out value="${stay.price}" /></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </jsp:body>

</petclinic:layout>
