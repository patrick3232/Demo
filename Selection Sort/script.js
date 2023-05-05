var blocksArray = [];
var swapArray = []; // swap array[id1, newindexid1, id2, newindexid2]
var blocksOnScreen;
var passes = 0;
var comparisons = 0;
var swaps = 0;

function getBlockAmount() {
    blocksArray = [];
    let data = document.getElementById("blocksAmount");
    let blocks = data.value;
    blocksOnScreen = blocks;
    let div = document.getElementById("div1");
    div.innerHTML = "";
    for (let i = 0; i < blocks; i++) {
        createBlock(i, i, i + 1, blocks);
    }
    data.value = "";
    document.getElementById("userForm").style.display = "none";
    document.getElementById("createBlocksBtn").style.display = "none";
    document.getElementById("shuffleBtn").style.display = "block";
}

function createBlock(id, left, height, blocks) {
    let canvas = document.createElement("CANVAS");
    canvas.width = 800 / blocks;
    canvas.height = (200 / blocks) * height;
    canvas.id = id;
    blocksArray.push(id);
    canvas.style.position = "absolute";
    canvas.style.left = canvas.width * left + "px";
    canvas.style.bottom = "0px";
    canvas.style.background = "red";
    let div = document.getElementById("div1");
    div.appendChild(canvas);
}

// CODE FROM https://www.w3resource.com/javascript-exercises/javascript-array-exercise-17.php
// DECREASE INDEX -> PICKS LAST INDEX -> CHOOSES RANDOM INDEX -> SWITCH THE INDEXES -> LOOP
function shuffleArray(arra1) {
    var ctr = arra1.length,
        temp,
        index;

    // While there are elements in the array
    while (ctr > 0) {
        // Pick a random index
        index = Math.floor(Math.random() * ctr);
        // Decrease ctr by 1
        ctr--;
        // And swap the last element with it
        temp = arra1[ctr];
        arra1[ctr] = arra1[index];
        arra1[index] = temp;
    }
    return arra1;
}

function shuffleBlocks() {
    blocksArray = shuffleArray(blocksArray);

    for (let i = 0; i < blocksArray.length; i++) {
        let block = document.getElementById(blocksArray[i]);
        block.style.left = block.width * i + "px";
    }
    document.getElementById("shuffleBtn").style.display = "none";
    document.getElementById("sortBtn").style.display = "block";
}

function sortBlocks() {
    console.log("begin sorting");

    let temp;
    let indexOfMinimum;
    let sorted = 0;

    // repeat until only one item in usorted data
    while (sorted < blocksArray.length - 1) {
        indexOfMinimum = sorted;

        // find the lowest item in the unsorted data
        for (i = sorted + 1; i < blocksArray.length; i++) {
            if (blocksArray[i] < blocksArray[indexOfMinimum]) {
                indexOfMinimum = i;
            }
            comparisons++;
        }
        // swap the lowest item with the first item in the unsorted data
        if (blocksArray[sorted] !== blocksArray[indexOfMinimum]) {
            // first item in unsorted data to temp
            temp = blocksArray[sorted];

            // minimum value to first in unsorted data
            blocksArray[sorted] = blocksArray[indexOfMinimum];

            // value of temp to where minimum was
            blocksArray[indexOfMinimum] = temp;
            swaps++;
        }
        let tempArray = [];
        tempArray.push(blocksArray[sorted]);
        tempArray.push(sorted);
        tempArray.push(blocksArray[indexOfMinimum]);
        tempArray.push(indexOfMinimum);
        swapArray.push(tempArray);
        sorted++;
        passes++;
    }
    console.log("loop has ended", blocksArray);

    requestAnimationFrame(updateScreen);
}

function setRed(canvasId) {
    document.getElementById(canvasId).style.background = "red";
}

function setGreen(canvasId) {
    document.getElementById(canvasId).style.background = "green";
}

function setYellow(canvasId) {
    document.getElementById(canvasId).style.background = "yellow";
}

var displayIndx = 0;
var fps = blocksOnScreen / 15;
// swap array[id1, newindexid1, id2, newindexid2]
function updateScreen(timestamp) {
    if (displayIndx !== passes) {
        let block1;
        let block2;
        setTimeout(function () {
            // get smallest block
            let id1 = swapArray[displayIndx][0];
            let id1Pos = swapArray[displayIndx][1];
            block1 = document.getElementById(id1);
            setYellow(id1);

            // get block smallest block is swapping with
            let id2 = swapArray[displayIndx][2];
            let id2Pos = swapArray[displayIndx][3];
            block2 = document.getElementById(id2);
            setYellow(id2);

            // swap the blocks
            block1.style.left = block1.width * id1Pos + "px";
            block2.style.left = block2.width * id2Pos + "px";
            setGreen(id1);
            // setRed(id2);

            displayIndx++;
            requestAnimationFrame(updateScreen);
        }, 1000 / fps);
    } else {
        console.log("passes", passes);
        console.log("comparisons", comparisons);
        console.log("swaps", swaps);
    }
}
