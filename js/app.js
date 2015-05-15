function requestOptions() {
  $.get("options.json", function(data) {
    loadOptions(data);
  });
}

function loadOptions(data) {
  var parsedOptions = JSON.parse(data);
  $("#selector").append($("<option></option>")
                .attr("value", "name")
                .text(parsedOptions.name));
}