$('input[name="daterange"]').on('click', function () {
    if($('p[id="wrongDateText"]')) {
        $('p[id="wrongDateText"]').remove();
    }
})

$('input[name="daterange"]').on('apply.daterangepicker', function (ev, picker) {
    if(validateDate(picker.startDate, picker.endDate, $('input[name="duration"]').val())){
        $(this).val(picker.startDate.format('MM/DD/YYYY') + ' - ' + picker.endDate.format('MM/DD/YYYY'));
        $('input[name="dateFrom"]').val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
        $('input[name="dateTo"]').val(picker.endDate.format('YYYY-MM-DD HH:mm:ss'));
    }
    else{
        $('<p style="color:red; text-align: center;" id="wrongDateText">Ви ввели занадто малий проміжок часу</p>').insertAfter('#pageTitle');
        $('#periodrange').val("");
    }

});

$('.rent').click(function(){
    console.log($(this).attr('id'));
    $('input[name="id_room"]').val($(this).attr('id'));
    $('input[name="filledDateFrom"]').val($('input[name="dateFrom"]').val());
    $('input[name="filledDateTo"]').val($('input[name="dateTo"]').val());
    document.getElementById('formToRent').submit()
});

$(function() {
    $('input[name="daterange"]').daterangepicker({
        autoUpdateInput: false,
        minDate: new Date(),
        opens: 'center'
    }, function(start, end, label) {
        console.log("A new date selection was made: " + start.format('DD/MM/YYYY HH:mm:ss.SSS') + ' to ' + end.format('DD/MM/YYYY HH:mm:ss.SSS'));
    });
});

function validateDate(startDate, endDate, duration){
    const newDate = new Date(startDate);
    switch (duration){
        case 'day' :
            return true;
        case 'week' :
            return (endDate - startDate) / (1000 * 60 * 60 * 24 * 6) >= 1;
        case 'month' :
            return (endDate - startDate)/ (1000 * 3600 * 24) >= getMonthDays(newDate.getMonth()) - 1;
        case 'year' :
            return (endDate - startDate)/ (1000 * 3600 * 24) >= 364;
    }

}

function getMonthDays(month){
    switch (month){
        case 0 : return 31;
        case 1 : return 28;
        case 2 : return 31;
        case 3 : return 30;
        case 4 : return 31;
        case 5 : return 30;
        case 6 : return 31;
        case 7 : return 31;
        case 8 : return 30;
        case 9 : return 31;
        case 10 : return 30;
        case 11 : return 31;
    }
}