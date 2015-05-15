function requestOptions() {
  $.get("options.json", function(data) {
    loadOptions(data);
  });
}

function loadOptions(data) {
  $("#selector").append($("<option></option>")
                .attr("value", "name")
                .text(data.name));
}
