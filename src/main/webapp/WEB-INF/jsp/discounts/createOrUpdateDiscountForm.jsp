<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="shops">
<jsp:attribute name="customScript">
	<script>
		$(function() {
			$("#startDate").datepicker({
				dateFormat : 'yy/mm/dd'
			});
		});
	</script>
	<script>
            $(function () {
                $("#finishDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
 </jsp:attribute>
 <jsp:body>
	<h2>
		<c:if test="${discount['new']}">New </c:if> Discount
	</h2>
	<table class="table table-striped">
		<tr>
			<th>Product</th>
			<td></td>
		</tr>
		<tr>
			<th>Name</th>
			<td><c:out value="${product.name}" /></td>
		</tr>
		<tr>
			<th>Price</th>
			<td><c:out value="${product.price}" /> EUR</td>
		</tr>
		<tr>
			<th>Stock</th>
			<td><c:out value="${product.stock}" /></td>
		</tr>
	</table>
	<br>
	<form:form modelAttribute="discount" class="form-horizontal"
		id="add-discount-form">
		<div class="form-group has-feedback">
			<petclinic:inputField label="Percentage" name="percentage" />
			<petclinic:inputField label="Start Date" name="startDate"/>
			<petclinic:inputField label="Finish Date" name="finishDate" />
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
			<c:if test="${discount['new']}">
                        <button class="btn btn-default" type="submit">Add Discount</button>
                   </c:if>
                    <c:if test="${!discount['new']}">
                        <button class="btn btn-default" type="submit">Update Discount</button>
                   </c:if>
			</div>
		</div>
	</form:form>
	</jsp:body>
</petclinic:layout>
