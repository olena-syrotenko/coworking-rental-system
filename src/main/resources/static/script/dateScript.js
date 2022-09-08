const from_input = $('#startingDate').pickadate(),
    from_picker = from_input.pickadate('picker');
const to_input = $('#endingDate').pickadate(),
    to_picker = to_input.pickadate('picker');

if (from_picker.get('value')) {
    to_picker.set('min', from_picker.get('select'))
}
if (to_picker.get('value')) {
    from_picker.set('max', to_picker.get('select'))
}

from_picker.on('set', function (event) {
    if (event.select) {
        to_picker.set('min', from_picker.get('select'))
    } else if ('clear' in event) {
        to_picker.set('min', false)
    }
})
to_picker.on('set', function (event) {
    if (event.select) {
        from_picker.set('max', to_picker.get('select'))
    } else if ('clear' in event) {
        from_picker.set('max', false)
    }
})