$(document).ready(function()
{
	$("#alertSuccess").hide();
	$("#alertError").hide();
});

// Save ==================================
$(document).on("click", "#btnSave", function(event) 
{
	// Clear alerts---------------------
	$("#alertSuccess").text("");
	$("#alertSuccess").hide();
	$("#alertError").text("");
	$("#alertError").hide();
	
	// Form validation-------------------
	var status = validateInquiryForm();
	if (status != true) 
	{
		$("#alertError").text(status);
		$("#alertError").show();
		return;
	}
	
	// If valid------------------------
	var type = ($("#hidInquiryIDSave").val() == "") ? "POST" : "PUT";
	
	$.ajax(
		{
			url: "PowerInterruptionsAPI",
			type: type,
			data: $("#formInquiry").serialize(),
			dataType: "text",
			complete: function(response, status) 
			{
				onInquirySaveComplete(response.responseText, status);
			}
		});
});

function onInquirySaveComplete(response, status) {
	if (status == "success") 
	{
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success") 
		{
			$("#alertSuccess").text("Successfully saved.");
			$("#alertSuccess").show();
			$("#divInquirysGrid").html(resultSet.data);
		} 
		else if (resultSet.status.trim() == "error") 
		{
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
	} 
	else if (status == "error") 
	{
		$("#alertError").text("Error while saving.");
		$("#alertError").show();
	} 
	else 
	{
		$("#alertError").text("Unknown error while saving..");
		$("#alertError").show();
	}
	
	$("#hidInquiryIDSave").val("");
	$("#formInquiry")[0].reset();
}

$(document).on("click", ".btnUpdate", function(event) {
	$("#hidInquiryIDSave").val($(this).data("itemid"));
	$("#customerName").val($(this).closest("tr").find('td:eq(0)').text());
	$("#phoneNumber").val($(this).closest("tr").find('td:eq(1)').text());
	$("#inquiryType").val($(this).closest("tr").find('td:eq(2)').text());
	$("#inquiryLocation").val($(this).closest("tr").find('td:eq(3)').text());
	$("#remarks").val($(this).closest("tr").find('td:eq(4)').text());
	
	
});

$(document).on("click", ".btnRemove", function(event) {
	$.ajax(
		{
			url: "PowerInterruptionsAPI",
			type: "DELETE",
			data: "inquiryID=" + $(this).data("itemid"),
			dataType: "text",
			complete: function(response, status) {
				onInquiryDeleteComplete(response.responseText, status);
			}
		});
});

function onInquiryDeleteComplete(response, status) {
	if (status == "success") {
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success") {
			$("#alertSuccess").text("Successfully deleted.");
			$("#alertSuccess").show();
			$("#divInquirysGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error") {
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
	} else if (status == "error") {
		$("#alertError").text("Error while deleting.");
		$("#alertError").show();
	} else {
		$("#alertError").text("Unknown error while deleting..");
		$("#alertError").show();
	}
}

// CLIENT-MODEL================================================================
function validateInquiryForm() {

	// Complainer Name
	if ($("#customerName").val().trim() == "") {
		return "Insert Customer Name.";
	}

	// Phone Number-------------------------------
	if ($("#phoneNumber").val().trim() == "") {
		return "Insert Phone Numbe.";
	}

	// is numerical value
	var tmpPhoneNumber = $("#phoneNumber").val().trim();
	if (!$.isNumeric(tmpPhoneNumber)) {
		return "Insert a numerical value for Phone Number.";
	}

	//  Inquiry Type------------------------
	if ($("#inquiryType").val().trim() == "") {
		return "Insert Inquiry Type.";
	}

	//  Inquiry Location------------------------
	if ($("#inquiryLocation").val().trim() == "") {
		return "Insert Inquiry Location.";
	}

	//  Remarks------------------------
	if ($("#remarks").val().trim() == "") {
		return "Insert Remarks.";
	}
	return true;
}

