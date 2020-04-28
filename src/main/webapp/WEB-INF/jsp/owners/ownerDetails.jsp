<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="owners">

	<h2>Owner Information</h2>


	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><b><c:out value="${owner.firstName} ${owner.lastName}" /></b></td>
		</tr>
		<tr>
			<th>Address</th>
			<td><c:out value="${owner.address}" /></td>
		</tr>
		<tr>
			<th>City</th>
			<td><c:out value="${owner.city}" /></td>
		</tr>
		<tr>
			<th>Telephone</th>
			<td><c:out value="${owner.telephone}" /></td>
		</tr>
	</table>

	<spring:url value="{ownerId}/edit" var="editUrl">
		<spring:param name="ownerId" value="${owner.id}" />
	</spring:url>
	<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Owner</a>

	<spring:url value="{ownerId}/pets/new" var="addUrl">
		<spring:param name="ownerId" value="${owner.id}" />
	</spring:url>
	<a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Pet</a>

	<br />
	<br />
	<h2>Pets, Visits, Stays and Hospitalisations</h2>

	<table class="table table-striped">
		<c:forEach var="pet" items="${owner.pets}">

			<tr>
				<td valign="top">
					<dl class="dl-horizontal">
						<dt>Name</dt>
						<dd>
							<c:out value="${pet.name}" />
						</dd>
						<dt>Birth Date</dt>
						<dd>
							<petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd" />
						</dd>
						<dt>Type</dt>
						<dd>
							<c:out value="${pet.type.name}" />
						</dd>
						<dt>Status</dt>
						<dd>
							<c:out value="${pet.status}" />
						</dd>
					</dl>
				</td>
				<td valign="top">
					<table class="table-condensed">
						<thead>
							<tr align="left">
								<th>Visits:</th>
								<th>Visit Date</th>
								<th>Description</th>
							</tr>
						</thead>
						<c:forEach var="visit" items="${pet.visits}">
							<tr>
								<td></td>
								<td><petclinic:localDate date="${visit.date}"
										pattern="yyyy-MM-dd" /></td>
								<td><c:out value="${visit.description}" /></td>
							</tr>
						</c:forEach>
						<thead>
							<tr align="left">
								<th>Stays:</th>
								<th>Start Date</th>
								<th>Finish Date</th>
								<th>Special Cares</th>
								<th>Price (EUR)</th>
							</tr>
						</thead>
						<c:forEach var="stay" items="${pet.stays}">
							<tr>
								<td></td>
								<td><petclinic:localDate date="${stay.startdate}"
										pattern="yyyy-MM-dd" /></td>
								<td><petclinic:localDate date="${stay.finishdate}"
										pattern="yyyy-MM-dd" /></td>
								<td><c:out value="${stay.specialCares}" /></td>
								<td><c:out value="${stay.price}" /></td>
								<sec:authorize access="hasAuthority('admin')">
								<c:if test="${!stay.pastStay()}">
								<td><spring:url
										value="/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit"
										var="stayUrl">
										<spring:param name="ownerId" value="${owner.id}" />
										<spring:param name="petId" value="${pet.id}" />
										<spring:param name="stayId" value="${stay.id}" />
									</spring:url> <a href="${fn:escapeXml(stayUrl)}">Update</a></td>
								</c:if>
								<td>
								<c:if test="${stay.activeStay()}">
           							 <spring:url
										value="/owners/{ownerId}/pets/{petId}/stays/{stayId}/end"
										var="stayUrl">
										<spring:param name="ownerId" value="${owner.id}" />
										<spring:param name="petId" value="${pet.id}" />
										<spring:param name="stayId" value="${stay.id}" />
                      				</spring:url> <a href="${fn:escapeXml(stayUrl)}">End Stay</a>
           						</c:if>
           						</sec:authorize>
           						</td>
							</tr>
						</c:forEach>
						<thead>
							<tr align="left">
								<th>Hospitalisations:</th>
								<th>Start Date</th>
								<th>Finish Date</th>
								<th>Diagnosis</th>
								<th>Treatment</th>
								<th>Status</th>
								<th>Total Price (EUR)</th>
							</tr>
						</thead>
						<c:forEach var="hospitalisation" items="${pet.hospitalisations}">
							<tr>
								<td></td>
								<td><petclinic:localDate
										date="${hospitalisation.startDate}" pattern="yyyy-MM-dd" /></td>
								<td><c:if test="${hospitalisation.hospitalisationStatus == 'DISCHARGED'}"><petclinic:localDate
										date="${hospitalisation.finishDate}" pattern="yyyy-MM-dd" /></c:if></td>
								<td><c:out value="${hospitalisation.diagnosis}" /></td>
								<td><c:out value="${hospitalisation.treatment}" /></td>
								<td><c:out value="${hospitalisation.hospitalisationStatus}" /></td>
								<td><c:out value="${hospitalisation.totalPrice}" /></td>
								<sec:authorize access="hasAuthority('admin')">
								<c:if test="${hospitalisation.hospitalisationStatus == 'HOSPITALISED'}">
								<td><spring:url
										value="/owners/{ownerId}/pets/{petId}/hospitalisations/{hospitalisationId}/edit"
										var="hospitaliationUrl">
										<spring:param name="ownerId" value="${owner.id}" />
										<spring:param name="petId" value="${pet.id}" />
										<spring:param name="hospitalisationId" value="${hospitalisation.id}" />
									</spring:url> <a href="${fn:escapeXml(hospitaliationUrl)}">Update</a></td>
								</c:if>
									
									<c:if test="${hospitalisation.hospitalisationStatus == 'DISCHARGED'}">
								<td><spring:url
										value="/owners/{ownerId}/pets/{petId}/hospitalisations/{hospitalisationId}/delete"
										var="hospitaliationDeleteUrl">
										<spring:param name="ownerId" value="${owner.id}" />
										<spring:param name="petId" value="${pet.id}" />
										<spring:param name="hospitalisationId" value="${hospitalisation.id}" />
									</spring:url> <a href="${fn:escapeXml(hospitaliationDeleteUrl)}">Remove</a></td>
									</c:if>
								</sec:authorize>
							</tr>
						</c:forEach>
						<tr>
						<sec:authorize access="hasAuthority('admin')">
						<td><spring:url value="/owners/{ownerId}/pets/{petId}/edit"
									var="petUrl">
									<spring:param name="ownerId" value="${owner.id}" />
									<spring:param name="petId" value="${pet.id}" />
								</spring:url> <a href="${fn:escapeXml(petUrl)}" class="btn btn-default">Edit Pet</a></td>
							<c:if test="${pet.status == 'HEALTHY'}">
								<td><spring:url
										value="/owners/{ownerId}/pets/{petId}/delete" var="removeUrl">
										<spring:param name="petId" value="${pet.id}" />
										<spring:param name="ownerId" value="${owner.id}" />
									</spring:url> <a href="${fn:escapeXml(removeUrl)}" class="btn btn-default">Remove Pet</a></td>
							</c:if>
							<td><spring:url
									value="/owners/{ownerId}/pets/{petId}/visits/new"
									var="visitUrl">
									<spring:param name="ownerId" value="${owner.id}" />
									<spring:param name="petId" value="${pet.id}" />
								</spring:url> <a href="${fn:escapeXml(visitUrl)}">Add Visit</a></td>
								<td><spring:url
									value="/owners/{ownerId}/pets/{petId}/stays/new" var="stayUrl">
									<spring:param name="ownerId" value="${owner.id}" />
									<spring:param name="petId" value="${pet.id}" />
								</spring:url> <a href="${fn:escapeXml(stayUrl)}">Add Stay</a></td>
							<c:if test="${pet.status == 'HEALTHY'}">
								<td><spring:url
										value="/owners/{ownerId}/pets/{petId}/hospitalisations/new"
										var="hospitalisationsUrl">
										<spring:param name="ownerId" value="${owner.id}" />
										<spring:param name="petId" value="${pet.id}" />
									</spring:url> <a href="${fn:escapeXml(hospitalisationsUrl)}">Hospitalise</a></td>
							</c:if>
							</sec:authorize>
							</tr>
					</table>
						<sec:authorize access="hasAuthority('admin')">
							<c:if test="${pet.status == 'SICK'}">
							<br />
								<spring:message text="This pet can not be deleted because it is sick."></spring:message>
							</c:if>
						</sec:authorize>
				</td>
			</tr>
		</c:forEach>
	</table>
</petclinic:layout>
