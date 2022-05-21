jq('.z-barcode').on('click', function(){
    window.imageSaver.save(getPngDataUrl());
});

function getPngDataUrl(){
    return document.querySelector('.z-barcode').toDataURL("image/png");
}