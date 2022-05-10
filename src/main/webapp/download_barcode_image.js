jq('.z-barcode').on('click', function(){
    var link = document.querySelector('.download');
    link.setAttribute('href', document.querySelector('.z-barcode').toDataURL("image/png"));
//    replace("image/png", "image/octet-stream")
    link.click();
});