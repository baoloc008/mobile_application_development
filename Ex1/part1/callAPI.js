$(document).ready(() => {
  $('#btnOK').click(() => {
    const latitude = $('#latitude').val();
    const longitude = $('#longitude').val();
    let address = 'not found';
    const url = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&key=AIzaSyCI5GJd1spjeLjSGCqHlcByDFTnfYQd3zU`;
    $.get(url, res => {
      if (res.status === 'OK') address = res.results[0].formatted_address;
    })
      .done(() => $('#result').html(`Address: ${address}`))
      .fail(err =>
        $('#result').html(`Error: ${err.responseJSON.error_message}`)
      );
  });
});
