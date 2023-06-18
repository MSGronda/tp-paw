function addOrUpdateParam(url, param, value) {
    const regex = new RegExp("([?&])" + param + "=.*?(&|$)", "i");
    const separator = url.indexOf('?') !== -1 ? "&" : "?";
    if (url.match(regex)) {
        return url.replace(regex, '$1' + param + "=" + value + '$2');
    }
    else
        return url + separator + param + "=" + value;
}

function removeURLParam(url, param) {
    const urlsections = url.split('?');

    if (urlsections.length >= 2) {
        const prefix = encodeURIComponent(param) + '=';
        const params = urlsections[1].split(/[&;]/g);

        for (let i = params.length - 1; i >= 0; i--) {
            if (decodeURIComponent(params[i]).lastIndexOf(prefix, 0) !== -1) {
                params.splice(i, 1);
            }
        }
        url = urlsections[0] + (params.length > 0 ? '?' + params.join('&') : '');
        return url;
    }
    else
        return url;
}