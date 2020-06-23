' **********************************************************************************
' Script for publishing batch reports for SFMS to shared folders
'
' Input parameter
'       Nil
' exit code:
'				0  : success
'       -1 : error
'
' Created/Updated by    Date            Description [Ref.]
' ------------------    -----------     --------------------------------------------
' Lily Mok              26 Jun 2018     Initial version
' **********************************************************************************
Option Explicit
'On Error Resume Next
Const ForReading = 1, ForWriting = 2, ForAppending = 8
'Const vbBinaryCompare=0, vbTextCompare=1

'Const IS_DEBUG=False

'Dim g_arrClusters
'Dim g_strBinFolderPath
Dim g_strSrcFolderPath
Dim g_strDestFolderPath
Dim g_strLogFile
'Dim g_strRemovePath

'Dim g_intNoOfFileDone
'Dim g_intNoOfFileSkipped

Dim g_strCrossDomain
Dim g_strRemoteUser
Dim g_strRemotePwd
Dim g_strRemoteShare
Dim g_strMapDrive
Dim g_strRemotePath

g_strSrcFolderPath				= "C:\MPRS\BatchReport\staging"
g_strDestFolderPath				= "\\corp.ha.org.hk\ha\HO\FD\IT\HAS\MPRS\BatchReport"
g_strLogFile					= "C:\MPRS\BatchReport\bin\log\MPRSReportBatch.log"

g_strCrossDomain				= "N"
g_strRemoteUser					= "CORP\mprs_support"
g_strRemotePwd					= ""
g_strRemoteShare				= "\\corp.ha.org.hk\ha\HO\FD\IT\HAS\MPRS"
g_strMapDrive					= "M:"
g_strRemotePath					= "BatchReport"

'---------------------------------------------------------------------------
' Functions
'---------------------------------------------------------------------------
Public Function fnCopyFile()
	Dim objFSO
	Dim strLine, strFile, strFileName, strSrcFilePath, strDestFilePath, strDestFileParentFolder
	
	Set objFSO = CreateObject("Scripting.FileSystemObject") 
		
	Call fnAppendTextToFile(g_strLogFile, "Start copying files form staging to publishing folders...")
	'Call fnAppendTextToFile(g_strLogFile, "Copy file: " & strSrcFilePath)
	'Call fnAppendTextToFile(g_strLogFile, "       To: " & strDestFilePath)
	Call fnAppendTextToFile(g_strLogFile, "Copy file: " & g_strSrcFolderPath)
	Call fnAppendTextToFile(g_strLogFile, "       To: " & g_strRemoteShare)
	
	'objFSO.MoveFile strSrcFilePath, strDestFilePath
	objFSO.CopyFile g_strSrcFolderPath & "\HCH\*", g_strDestFolderPath & "\HCH\Medical\", True
	objFSO.CopyFile g_strSrcFolderPath & "\HEC\*", g_strDestFolderPath & "\HEC\Medical\", True
	objFSO.CopyFile g_strSrcFolderPath & "\HWC\*", g_strDestFolderPath & "\HWC\Medical\", True
	objFSO.CopyFile g_strSrcFolderPath & "\KCC\*", g_strDestFolderPath & "\KCC\Medical\", True
	objFSO.CopyFile g_strSrcFolderPath & "\KEC\*", g_strDestFolderPath & "\KEC\Medical\", True
	objFSO.CopyFile g_strSrcFolderPath & "\KWC\*", g_strDestFolderPath & "\KWC\Medical\", True
	objFSO.CopyFile g_strSrcFolderPath & "\NEC\*", g_strDestFolderPath & "\NEC\Medical\", True
	objFSO.CopyFile g_strSrcFolderPath & "\NWC\*", g_strDestFolderPath & "\NWC\Medical\", True

	objFSO.DeleteFile g_strSrcFolderPath & "\HCH\*"
	objFSO.DeleteFile g_strSrcFolderPath & "\HEC\*"
	objFSO.DeleteFile g_strSrcFolderPath & "\HWC\*"
	objFSO.DeleteFile g_strSrcFolderPath & "\KCC\*"
	objFSO.DeleteFile g_strSrcFolderPath & "\KEC\*"
	objFSO.DeleteFile g_strSrcFolderPath & "\KWC\*"
	objFSO.DeleteFile g_strSrcFolderPath & "\NEC\*"
	objFSO.DeleteFile g_strSrcFolderPath & "\NWC\*"
			
	Call fnAppendTextToFile(g_strLogFile, "End copy files")
	fnCopyFile = True

	'Call fnAppendTextToFile(g_strLogFile, "No of files copied: " & g_intNoOfFileDone)
	'Call fnAppendTextToFile(g_strLogFile, "No of files skipped:" & g_intNoOfFileSkipped)
		
	Set objFSO = Nothing
End Function

Public Function fnPatChar(ByVal a_int, ByVal a_chr, ByVal a_intNoOfDigit)
	fnPatChar = String(a_intNoOfDigit - Len(a_int), a_chr) & a_int
End Function

Public Function fnArchive
	Dim objFSO
	Dim datCur, strDateTime
	
	Set objFSO = CreateObject("Scripting.FileSystemObject")
	
	' archive the files
	datCur = Now
	strDateTime = Year(datCur) & fnPatChar(Month(datCur), "0", 2) & fnPatChar(Day(datCur), "0", 2) & fnPatChar(Hour(datCur), "0", 2) & fnPatChar(Minute(datCur), "0", 2) & fnPatChar(Second(datCur), "0", 2)
	
	If objFSO.FileExists(g_strLogFile) Then
		objFSO.MoveFile g_strLogFile, objFSO.GetParentFolderName(g_strLogFile) & "\" & objFSO.GetBaseName(g_strLogFile) & "_" & strDateTime & ".log"
	End If
	
	Set objFSO = Nothing
	
End Function

Sub fnCreateFolders(ByVal a_strParentFolder, ByVal a_strSubFolder)
   Dim objFSO
   Dim strPath, strNewFolder
    
   Set objFSO = CreateObject("Scripting.FileSystemObject")
   
   strPath = a_strSubFolder

   If Right(strPath, 1) <> "\" Then
      strPath = strPath & "\"
   End If

   strNewFolder = ""
   Do Until strPath = strNewFolder
      strNewFolder = Left(strPath, InStr(Len(strNewFolder) + 1, strPath, "\"))
    
      If objFSO.FolderExists(a_strParentFolder & strNewFolder) = False Then
         objFSO.CreateFolder(a_strParentFolder & strNewFolder)
      End If
   Loop
   
   Set objFSO = Nothing
End Sub

Public Sub fnAppendTextToFile(ByVal a_strFilePath, ByVal a_strAppendingText)
    Dim objFSO, objF
    Dim datCur, strCurrentTime

    Set objFSO = CreateObject("Scripting.FileSystemObject")    
    
    'If objFSO.FileExists(a_strFilePath) Then
    	Set objF = objFSO.OpenTextFile(a_strFilePath, ForAppending, True)
    
    	datCur = Now
    	strCurrentTime = Year(datCur) & "-" & fnPatChar(Month(datCur), "0", 2) & "-" & fnPatChar(Day(datCur), "0", 2) & " " & fnPatChar(Hour(datCur), "0", 2) & ":" & fnPatChar(Minute(datCur), "0", 2) & ":" & fnPatChar(Second(datCur), "0", 2)

	    objF.WriteLine "[" & strCurrentTime & "] " & a_strAppendingText
  	  objF.Close
    	Set objF = Nothing
   	'End If
   	
    Set objFSO = Nothing
End Sub

Public Sub fnFinal(ByVal a_intExitCode)
	' release object
	'Set objDictConf = Nothing

	WScript.Quit(a_intExitCode)
End Sub


Public Function fnMapDrive(a_strLocalDriveLetter, a_strRemoteShare, a_strRemotePath, a_strUsr, a_strPas) 

    Dim i 
    Dim blnSkipMapping
    Dim objDrives, objNetwork

    blnSkipMapping = False    
    Set objNetwork = WScript.CreateObject("WScript.Network")
    Set objDrives = objNetwork.EnumNetworkDrives

		Call fnAppendTextToFile(g_strLogFile, "Mapping network drive...")

    If a_strLocalDriveLetter <> "" Then
        For i = 0 to objDrives.Count - 1 Step 2

            'The drive letter already in used but not mapped to the desired Remote Share
            If objDrives.Item(i) = a_strLocalDriveLetter And objDrives.Item(i+1) <> a_strRemoteShare Then
            	Call fnAppendTextToFile(g_strLogFile, "Drive Letter " & a_strLocalDriveLetter & " already in used, disconnecting...")
              objNetwork.RemoveNetworkDrive a_strLocalDriveLetter
              Call fnAppendTextToFile(g_strLogFile, "Drive " & a_strLocalDriveLetter & " disconnected.")
            End If
            
            'Remote folder is already mapped but to different Drive Letter        
            If objDrives.Item(i) <> a_strLocalDriveLetter And objDrives.Item(i+1) = a_strRemoteShare Then
            		Call fnAppendTextToFile(g_strLogFile, "Remote Folder " & a_strRemoteShare & " already mapped to " & objDrives.Item(i) & ", disconnecting...")
                'objNetwork.RemoveNetworkDrive a_strLocalDriveLetter
                objNetwork.RemoveNetworkDrive objDrives.Item(i)
                Call fnAppendTextToFile(g_strLogFile, "Remote Folder " & a_strRemoteShare & " disconnected.")
            End If
            
            'Remote folder already mapped to correct drive
            If objDrives.Item(i) = a_strLocalDriveLetter And objDrives.Item(i+1) = a_strRemoteShare Then
            		Call fnAppendTextToFile(g_strLogFile, "Remote Folder " & a_strRemoteShare & " already mapped to " & a_strLocalDriveLetter & ". No need to map again.")
                blnSkipMapping = True
            End If            
        Next
    Else    
        For i = 0 to objDrives.Count - 1 Step 2
            'Remote folder is already mapped, disconnect it
            If objDrives.Item(i+1) = a_strRemoteShare Then
            
            		Call fnAppendTextToFile(g_strLogFile, "Remote Folder " & a_strRemoteShare & " already mapped, disconnecting...")
                
                If objDrives.Item(i) <> "" Then   'The mapped drive have a drive letter
                    objNetwork.RemoveNetworkDrive objDrives.Item(i)
                Else
                    objNetwork.RemoveNetworkDrive a_strRemoteShare
                End If
                Call fnAppendTextToFile(g_strLogFile, "Remote Folder " & a_strRemoteShare & " disconnected.")
            End If
        Next
    End If

    If Not blnSkipMapping Then
      	On Error Resume Next
      	Err.Clear
        Call fnAppendTextToFile(g_strLogFile, "Mapping " & IIf(a_strLocalDriveLetter="", "{None}", a_strLocalDriveLetter) & " to " & a_strRemoteShare & "...")
        If a_strUsr <> "" Or a_strPas <> "" Then
            objNetwork.MapNetworkDrive a_strLocalDriveLetter, a_strRemoteShare, False, a_strUsr, a_strPas
        Else
            objNetwork.MapNetworkDrive a_strLocalDriveLetter, a_strRemoteShare, False
        End If
        
        If Err.Number <> 0 Then
        	Call fnAppendTextToFile(g_strLogFile, a_strRemoteShare & " cannot be mapped")
        	Call fnAppendTextToFile(g_strLogFile, "Error (" & Err.Number & ") - " & Err.Description)
					fnMapDrive = False
				Else
					fnMapDrive = True
        	Call fnAppendTextToFile(g_strLogFile, IIf(a_strLocalDriveLetter="", "{None}", a_strLocalDriveLetter) & " mapped.")
				End If
		Else
			fnMapDrive = True
    End If

		g_strDestFolderPath = a_strLocalDriveLetter & "\" & a_strRemotePath

		Set objDrives = Nothing
  	Set objNetwork = Nothing
End Function

Public Sub fnDisconnectNetworkPath(a_strLocalDriveLetter, a_strRemoteShare)
   Dim sPath
   Dim objNetwork
   
   Call fnAppendTextToFile(g_strLogFile, "Disconnecting " & sPath & " ...")
   
   On Error Resume Next
   Err.Clear
   
   sPath = IIf(a_strLocalDriveLetter = "", a_strRemoteShare, a_strLocalDriveLetter)
   Set objNetwork = WScript.CreateObject("WScript.Network")

   objNetwork.RemoveNetworkDrive sPath
    
   If Err.Number <> 0 Then
			Call fnAppendTextToFile(g_strLogFile, "Error occur when disconnecting " & sPath)
			Call fnAppendTextToFile(g_strLogFile, "Error (" & Err.Number & ") - " & Err.Description)
   Else
    	Call fnAppendTextToFile(g_strLogFile, sPath & " disconnected")
   End If
        
   Set objNetwork = Nothing
End Sub

Public Function IIf(a_strTest, a_strTrue, a_strFalse)
    If a_strTest Then
        IIf = a_strTrue
    Else 
        IIf = a_strFalse
    End If
End Function

'---------------------------------------------------------------------------
' Main
'---------------------------------------------------------------------------

' map network (if any)
If g_strCrossDomain = "Y" Then
	If Not fnMapDrive(g_strMapDrive, g_strRemoteShare, g_strRemotePath, g_strRemoteUser, g_strRemotePwd) Then
		Call fnFinal(-1)
	End If
End If

' start copy
If Not fnCopyFile() Then
	If g_strCrossDomain = "Y" Then
		Call fnDisconnectNetworkPath(g_strMapDrive, g_strRemotePath)
	End If
	
	Call fnArchive
	Call fnFinal(-1)
End If

' archive staging files
If g_strCrossDomain = "Y" Then
	Call fnDisconnectNetworkPath(g_strMapDrive, g_strRemotePath)
End If
Call fnArchive

' clear up	
Call fnFinal(0)
