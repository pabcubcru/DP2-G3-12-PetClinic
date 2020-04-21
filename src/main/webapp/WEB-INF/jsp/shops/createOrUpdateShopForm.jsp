<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="shops">
    <h2>
        <c:if test="${shop['new']}">New </c:if> Shop
    </h2>
    <form:form modelAttribute="shop" class="form-horizontal" id="update-shop-form">
        <div class="form-group has-feedback">
       		<petclinic:inputField label="Name" name="name"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10" align="right">
                <c:choose>
                    <c:when test="${shop['new']}" >
                        <button class="btn btn-default" type="submit">Create</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
