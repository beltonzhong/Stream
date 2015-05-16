function requestOptions() {
  $.get("options", function(data) {
    loadOptions(data);
  });
}

function loadOptions(data) {
  var parsedOptions = JSON.parse(data);
  $("selector").append($("<option></option")
  	       .attr("value", "ABCD")
	       .text("ABCD"));
  /*
  $.each(parsedOptions.names, function(index, value) {
    $("selector").append($("<option></option")
                 .attr("value", value)
                 .text(value));
  });
  */
}
