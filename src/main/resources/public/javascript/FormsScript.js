function hideRedskabsrum() {
    //document.getElementById("redskabsrum1").style.display = "none";
    //document.getElementById("redskabsrum2").style.display = "none";

    document.getElementById("redskabsrum1").style.visibility = "hidden";
    document.getElementById("redskabsrum2").style.visibility = "hidden";
    document.getElementById("redskabsrum_længde").selectedIndex = 0;
    document.getElementById("redskabsrum_bredde").selectedIndex = 0;
}

function showRedskabsrum() {
    //document.getElementById("redskabsrum1").style.display = "contents";
    //document.getElementById("redskabsrum2").style.display = "contents";
    document.getElementById("redskabsrum1").style.visibility = "visible";
    document.getElementById("redskabsrum2").style.visibility = "visible";
    document.getElementById("redskabsrum_længde").selectedIndex = 0;
    document.getElementById("redskabsrum_bredde").selectedIndex = 0;

}


