#global variables
$global:baseName 
$global:directory

#FUNCTIONS

# THIS PRODUCES THE WELCOME SCREEN EXPLAINING THE SCRIPTS ACTIONS
function welcome {
    $Shell = New-Object -ComObject "WScript.Shell"
    $Button = $Shell.Popup('This script will rename every file within the directory that you specify. Renaming starts from 1 and increments. A base name may be added at the start of file name if specified. 

Example: "1.txt" or "basename1.txt"', 0, "Hello", 0)
cls
}

# THIS ASKS THE USER IF THEY WOULD LIKE A BASE NAME
function askForBaseName{


$namingChoice=read-host -prompt "would you like a base name? yes(y)/no(n)"
if ($namingChoice-eq"yes" -or $namingChoice-eq"y") {
   cls
   write-host "Yes was entered"
   createBaseName
  
} elseif ($namingChoice-eq"no" -or $namingChoice-eq"n") {
    cls
    write-host "No basename will be used"
    createDirectoryNameNoBaseName
} else {
    cls
    write-host "Please enter correct value"
    askForBaseName
}

}

#CREATES DIRECTORY NAME WHEN NO BASE NAME IS BEING USED
function createDirectoryNameNoBaseName {
    
    $global:directory=read-host -prompt "Please enter the full directory path of files to be renamed"
    #revision 1
    cls 
    verifyDirectoryNameNoBaseName
}


function verifyDirectoryNameNoBaseName {
    write-host "The directory name you entered was: $global:directory"
    $verify=read-host -prompt "Is this correct  yes(y)/no(n)"
    if ($verify-eq"yes" -or $verify-eq"y") {
    cls
    renameFilesNoBaseName
} elseif ($verify-eq"no" -or $verify-eq"n") {
    cls
    createDirectoryNameNoBaseName
} else {
    cls
    write-host "Please enter correct value"
    verifyDirectoryNameNoBaseName
}


}



# THIS RENAMES EACH FILE INSIDE THE SPECIFIED DIRECTORY WHEN NO BASENAME IS BEING USED
function renameFilesNoBaseName {
    cd $global:directory
    $items = Get-ChildItem
    $i = 0
    
    foreach ($item in $items) {
    $i++
    #split the name of file at the . to obtain file extension then rename file
    $splitName=$item.name.split(".")
    rename-item -path ($global:directory + "\" + $item.name) -newname ("$i" + "." + $splitName[$splitName.length - 1])
    write-host "renaming of $item is complete"
    }
}


function createBaseName {
    
    $global:baseName=read-host -prompt "Please enter your desired base name"
    cls
    verifyBaseName
}











function verifyBaseName {
    write-host "The basename you entered was: $baseName"
    $verify=read-host -prompt "Is this correct  yes(y)/no(n)"
    if ($verify-eq"yes" -or $verify-eq"y") {
    cls
    write-host "Your basename is: $baseName"
    createDirectoryNameWithBaseName
} elseif ($verify-eq"no" -or $verify-eq"n") {
    cls
    createBaseName
} else {
    cls
    write-host "Please enter correct value"
    verifyBaseName
}


}

#CREATES DIRECTORY NAME WHEN A BASE NAME IS BEING USED
function createDirectoryNameWithBaseName {
    
    $global:directory=read-host -prompt "Please enter the full directory path of files to be renamed"
    #revision 1
    cls 
    verifyDirectoryNameWithBaseName
}


function verifyDirectoryNameWithBaseName {
    write-host "The directory name you entered was: $global:directory"
    $verify=read-host -prompt "Is this correct  yes(y)/no(n)"
    if ($verify-eq"yes" -or $verify-eq"y") {
    cls
    renameFilesWithBaseName
} elseif ($verify-eq"no" -or $verify-eq"n") {
    cls
    createDirectoryNameWithBaseName
} else {
    cls
    write-host "Please enter correct value"
    verifyDirectoryNameWithBaseName
}


}



# THIS RENAMES EACH FILE INSIDE THE SPECIFIED DIRECTORY WHEN A BASENAME IS BEING USED
function renameFilesWithBaseName {
    cd $global:directory
    $items = Get-ChildItem
    $i = 0
    
    foreach ($item in $items) {
    $i++
    #split the name of file at the . to obtain file extension then rename file
    $splitName=$item.name.split(".")
    rename-item -path ($global:directory + "\" + $item.name) -newname ("$global:baseName" + "$i" + "." + $splitName[$splitName.length - 1])
    write-host "renaming of $item is complete"

    }
}


function finished {
    $shell = new-object -ComObject "Wscript.shell"
    $button = $shell.popup("All files have been renamed", 0, "Goodbye", 0) 
}
# CALL FUNCTIONS
welcome
askForBaseName
finished














# FUTURE REVISIONS
#revision 1 - could search for the directory to see if it actually exists if not then prompt user 