<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="vets">

<jsp:body>
        <h2>
            <c:if test="${vet['new']}">New </c:if> Vet
        </h2>
        <form:form modelAttribute="vet"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${vet.id}"/>
            <div class="form-group has-feedback">
                <petclinic:inputField label="FirstName" name="firstName"/>
                <petclinic:inputField label="LastName" name="lastName"/>
				<select multiple name="listaSpe">
					<c:forEach items="${listSpecialties}" var="item">
						<c:if test="${fn:contains(vet.specialties, item)}">
							<option value="${item}" selected="selected">${item}</option>
						</c:if>
						<c:if test="${not fn:contains(vet.specialties, item)}">
							<option value="${item}">${item}</option>
						</c:if>
					</c:forEach>
				</select>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${vet['new']}">
                            <button class="btn btn-default" type="submit">Add Vet</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Update Vet</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <c:if test="${!vet['new']}">
        </c:if>
    </jsp:body>
</petclinic:layout>