;CREATE MAIN MENU
Global menuWindow
Global menuTab
Global menuTabGraphics
Global menuScreenSettings
Global menuScreenResolutionLabel
Global menuScreenResolution
Global gfxModes
Global menuScreenModeLabel
Global menuScreenFullScreen
Global menuScreenWindowed
Global menuScreenVSync

Global menuTabControls
Global menuControlsMotion
Global menuControlsForwardLabel
Global menuControlsForwardButton
Global menuControlsBackwardLabel
Global menuControlsBackwardButton
Global menuControlsLeftLabel
Global menuControlsLeftButton
Global menuControlsRightLabel
Global menuControlsRightButton
Global menuControlsJumpLabel
Global menuControlsJumpButton
Global menuControlsVehicle
Global menuControlsEnterLabel
Global menuControlsEnterButton
Global menuControlsHandBrakeLabel
Global menuControlsHandBrakeButton
Global menuControlsHornLabel
Global menuControlsHornButton
Global menuControlsLightLabel
Global menuControlsLightButton
Global menuControlsRadioLabel
Global menuControlsRadioButton
Global menuControlsWeapons
Global menuControlsAttackLabel
Global menuControlsAttackButton
Global menuControlsNextWeaponLabel
Global menuControlsNextWeaponButton
Global menuControlsPreviousWeaponLabel
Global menuControlsPreviousWeaponButton
Global menuControlsSingleplayer
Global menuControlsFastSaveLabel
Global menuControlsFastSaveButton
Global menuControlsMultiplayer
Global menuControlsChatLabel
Global menuControlsChatButton
Global menuControlsSpecial
Global menuControlsFartLabel
Global menuControlsFartButton
Global menuControlsMobLabel
Global menuControlsMobButton
Global menuControlsPowerUpLabel
Global menuControlsPowerUpButton

Global menuTabGameplay


Global menuTabSound
Global menuSoundVolume
Global menuSoundVolumeAmbient
Global menuSoundVolumeSpeech
Global menuSoundVolumeEffects
Global menuSoundVolumeMusic
Global volumeAmbient
Global volumeSpeech
Global volumeEffects
Global volumeMusic
Global menuSoundRadio
Global menuSoundRadioOn
Global menuSoundRadioOff
Global menuTabLanguage
Global menuLanguageBritishEnglish
Global menuLanguageBritishEnglishLabel
Global menuLanguageAmericanEnglish
Global menuLanguageAmericanEnglishLabel
Global menuLanguageGerman
Global menuLanguageGermanLabel
Global menuLanguageFrench
Global menuLanguageFrenchLabel
Global menuLanguageSpanish
Global menuLanguageSpanishLabel
Global menuLanguageItalian
Global menuLanguageItalianLabel
Global menuLanguagePortuguese
Global menuLanguagePortugueseLabel
Global menuLanguagePolish
Global menuLanguagePolishLabel
Global menuLanguageDanish
Global menuLanguageDanishLabel
Global menuLanguageSwedish
Global menuLanguageSwedishLabel
Global menuLanguageDutch
Global menuLanguageDutchLabel
Global menuLanguageTurkish
Global menuLanguageTurkishLabel

Global menuTabCheats
Global menuTabCheatsInfo
Global menuTabCheatsPlayerModifier
Global menuTabCheatsPlayerModifierInvulnerability
Global menuTabCheatsPlayerModifierArmor
Global menuTabCheatsPlayerModifierAllWeapons
Global menuTabCheatsPlayerModifierInfinityAmmo
Global menuTabCheatsPlayerModifierCarInvulnerable
Global menuTabCheatsPlayerModifierCarDoorsLocked

Global menuTabEditor
Global menuTabEditorControls
Global menuTabEditorControlsForwardLabel
Global menuTabEditorControlsForwardButton
Global menuTabEditorControlsBackwardLabel
Global menuTabEditorControlsBackwardButton
Global menuTabEditorControlsLeftLabel
Global menuTabEditorControlsLeftButton
Global menuTabEditorControlsRightLabel
Global menuTabEditorControlsRightButton
Global menuTabEditorControlsFastLabel
Global menuTabEditorControlsFastButton
Global menuTabEditorControlsPlaceManyLabel
Global menuTabEditorControlsPlaceManyButton
Global menuTabEditorHowToUse
Global menuTabEditorHowTo1
Global menuTabEditorHowTo2
Global menuTabEditorHowTo3
Global menuTabEditorHowTo4
Global menuTabEditorHowTo5
Global menuTabEditorHowTo6
Global menuTabEditorHowTo7
Global menuTabEditorHowTo8
Global menuTabEditorHowTo9
Global menuTabEditorHowTo10
Global menuTabEditorStart

Global mainEditorWindow
Global tabMain
Global tabBuildings
Global tabVehicles
Global tabHumans
Global tabItems
Global tabTriggers

Global menuTabCredits
Global menuTabCreditsProgramming
Global menuTabCreditsProgrammingLabel
Global menuTabCreditsSounds
Global menuTabCreditsSoundsLabel1
Global menuTabCreditsSoundsLabel2
Global menuTabCreditsSoundsLabel3
Global menuTabCreditsMusic
Global menuTabCreditsMusicLabel
Global menuTabCreditsGraphics
Global menuTabCreditsGraphicsLabel1
Global menuTabCreditsGraphicsLabel2
Global menuTabCreditsGraphicsLabel3
Global menuTabCreditsTools
Global menuTabCreditsToolsLabel1
Global menuTabCreditsToolsLabel2
Global menuTabCreditsToolsLabel3
Global menuTabCreditsToolsLabel4
Global menuTabCreditsToolsLabel5
Global menuTabCreditsToolsLabel6
Global menuTabCreditsSpecialThanks
Global menuTabCreditsSpecialThanksLabel1
Global menuTabCreditsSpecialThanksLabel2
Global menuTabCreditsSpecialThanksLabel3
Global menuTabCreditsSpecialThanksLabel4
Global menuTabCreditsSpecialThanksLabel5

Global menuSinglePlayer
Global menuSinglePlayerMapsLabel
Global menuSinglePlayerMaps
Global menuSinglePlayerStartNewGame
Global menuSinglePlayerLoadSaveGame

Global menuLocalNetwork
Global menuLocalNetworkHostMapsLabel
Global menuLocalNetworkHostMaps
Global hostWaitForClientsWindow
Global menuLocalNetworkHostGame
Global menuLocalNetworkJoinGame
Global menuInternet

Function InitMenu2()
	menuWindow = GUI_CreateWindow(GetInt("graphics","screenWidth")/2-100, GetInt("graphics","screenWidth")/2-150, 200, 300, "Bounty Hunt City", "", False, False, False)
	GUI_Message(menuWindow, "setlocked", True)
End Function

Function InitMenu()
	DeleteVars("tabGraphics")
	DeleteVars("tabControls")
	DeleteVars("tabSound")
	DeleteVars("tabLanguage")
	DeleteVars("tabCheats")
	DeleteVars("tabEditor")
	DeleteVars("tabCredits")
	DeleteVars("singlePlayer")
	DeleteVars("multiPlayer")
	DeleteVars("quit")
	DeleteVars("keyname")
	For v.Var = Each Var
		If(v\category = "language" And v\name = "dir") Then
			v\value = languageSelected
			Exit
		EndIf
	Next
	InitAll(LANGUAGE_PATH+languageSelected+"/menu.ini")
	InitAll(LANGUAGE_PATH+languageSelected+"/keynames.ini","keyname")
	
	
	;CREATE MAIN MENU
	menuWindow = GUI_CreateWindow(GetInt("graphics","screenWidth")/2-350, GetInt("graphics","screenHeight")/2-250, 700, 500, "Bounty Hunt City", "", False, False, False)
	GUI_Message(menuWindow, "setlocked", True)
	
	menuTab = GUI_CreateTab(menuWindow, 20, 10, 660, 300)
	
	menuTabGraphics = GUI_CreateTabPage(menuTab, GetString("tabGraphics","tabTitle"))
	menuScreenSettings = GUI_CreateGroupBox(menuTabGraphics, 10, 10, 280, 260, GetString("tabGraphics","screenSettings"))
	menuScreenResolutionLabel = GUI_CreateLabel(menuScreenSettings, 60, 22, GetString("tabGraphics","resolution"), "center")
	menuScreenResolution = GUI_CreateListBox(menuScreenSettings, 20, 40, 100, 100)
	gfxModes = CountGfxModes3D()
	Local validMode=0
	For currentMode=1 To gfxModes
		If(currentmode=1) Then
			If(GfxModeWidth(currentMode)>=800 And GfxModeHeight(currentMode)>=600) Then
				GUI_Message(menuScreenResolution, "additem", -1, GfxModeWidth(currentMode) + " x " + GfxModeHeight(currentMode))
			;select?
				If(GetInt("graphics","screenWidth")=GfxModeWidth(currentMode) And GetInt("graphics","screenHeight")=GfxModeHeight(currentMode)) Then
					GUI_Message(menuScreenResolution, "setselected", validMode)
				EndIf
				validMode=validMode+1
			EndIf
		Else
			If(GfxModeWidth(currentMode) > GfxModeHeight(currentMode)) Then
				If(GfxModeWidth(currentMode-1) <= GfxModeWidth(currentMode)) Then
					If(GfxModeWidth(currentMode)>=800 And GfxModeHeight(currentMode)>=600) Then
						GUI_Message(menuScreenResolution, "additem", -1, GfxModeWidth(currentMode) + " x " + GfxModeHeight(currentMode))
					;select?
						If(GetInt("graphics","screenWidth")=GfxModeWidth(currentMode) And GetInt("graphics","screenHeight")=GfxModeHeight(currentMode)) Then
							GUI_Message(menuScreenResolution, "setselected", validMode)
						EndIf
						validMode=validMode+1
					EndIf
				Else
					Exit
				EndIf
			EndIf
		EndIf
	Next
	menuScreenModeLabel = GUI_CreateLabel(menuScreenSettings, 60, 175, GetString("tabGraphics","displayMode"), "center")
	menuScreenFullScreen = GUI_CreateRadio(menuScreenSettings, 20, 200, GetString("tabGraphics","fullScreen"), 0)
	If(GetInt("graphics","screenMode")=1) Then GUI_Message(menuScreenFullScreen, "setchecked")
	menuScreenWindowed = GUI_CreateRadio(menuScreenSettings, 20, 220, GetString("tabGraphics","window"), 0)
	If(GetInt("graphics","screenMode")=2) Then GUI_Message(menuScreenWindowed, "setchecked")
	menuScreenVSync = GUI_CreateCheckBox(menuScreenSettings, 180, 120, " "+GetString("tabGraphics","vsync"))
	If(vsync) Then GUI_Message(menuScreenVSync, "setchecked", True)
	
	menuTabControls = GUI_CreateTabPage(menuTab, GetString("tabControls","tabTitle"))
	menuControlsMotion = GUI_CreateGroupBox(menuTabControls, 10, 10, 190, 180, GetString("tabControls","motion"))
	menuControlsForwardLabel = GUI_CreateLabel(menuControlsMotion, 110, 24, GetString("keyname",GetInt("key","forward")))
	menuControlsForwardButton = GUI_CreateButton(menuControlsMotion, 10, 20, 90, 20, GetString("tabControls","forward"))
	menuControlsBackwardLabel = GUI_CreateLabel(menuControlsMotion, 110, 54, GetString("keyname",GetInt("key","backward")))
	menuControlsBackwardButton = GUI_CreateButton(menuControlsMotion, 10, 50, 90, 20, GetString("tabControls","backward"))
	menuControlsLeftLabel = GUI_CreateLabel(menuControlsMotion, 110, 84, GetString("keyname",GetInt("key","left")))
	menuControlsLeftButton = GUI_CreateButton(menuControlsMotion, 10, 80, 90, 20, GetString("tabControls","left"))
	menuControlsRightLabel = GUI_CreateLabel(menuControlsMotion, 110, 114, GetString("keyname",GetInt("key","right")))
	menuControlsRightButton = GUI_CreateButton(menuControlsMotion, 10, 110, 90, 20, GetString("tabControls","right"))
	menuControlsJumpLabel = GUI_CreateLabel(menuControlsMotion, 110, 144, GetString("keyname",GetInt("key","jump")))
	menuControlsJumpButton = GUI_CreateButton(menuControlsMotion, 10, 140, 90, 20, GetString("tabControls","jump"))
	menuControlsVehicle = GUI_CreateGroupBox(menuTabControls, 220, 10, 190, 180, GetString("tabControls","vehicle"))
	menuControlsEnterLabel = GUI_CreateLabel(menuControlsVehicle, 110, 24, GetString("keyname",GetInt("key","enter_vehicle")))
	menuControlsEnterButton = GUI_CreateButton(menuControlsVehicle, 10, 20, 90, 20, GetString("tabControls","enterExit"))
	menuControlsHandBrakeLabel = GUI_CreateLabel(menuControlsVehicle, 110, 54, GetString("keyname",GetInt("key","handbrake")))
	menuControlsHandBrakeButton = GUI_CreateButton(menuControlsVehicle, 10, 50, 90, 20, GetString("tabControls","handBrake"))
	menuControlsHornLabel = GUI_CreateLabel(menuControlsVehicle, 110, 84, GetString("keyname",GetInt("key","horn")))
	menuControlsHornButton = GUI_CreateButton(menuControlsVehicle, 10, 80, 90, 20, GetString("tabControls","horn"))
	menuControlsLightLabel = GUI_CreateLabel(menuControlsVehicle, 110, 114, GetString("keyname",GetInt("key","light")))
	menuControlsLightButton = GUI_CreateButton(menuControlsVehicle, 10, 110, 90, 20, GetString("tabControls","light"))
	menuControlsRadioLabel = GUI_CreateLabel(menuControlsVehicle, 110, 144, GetString("keyname",GetInt("key","radio")))
	menuControlsRadioButton = GUI_CreateButton(menuControlsVehicle, 10, 140, 90, 20, GetString("tabControls","radio"))
	menuControlsWeapons = GUI_CreateGroupBox(menuTabControls, 430, 10, 190, 120, GetString("tabControls","weapons"))
	menuControlsAttackLabel = GUI_CreateLabel(menuControlsWeapons, 110, 24, GetString("keyname",GetInt("key","attack")))
	menuControlsAttackButton = GUI_CreateButton(menuControlsWeapons, 10, 20, 90, 20, GetString("tabControls","attack"))
	menuControlsNextWeaponLabel = GUI_CreateLabel(menuControlsWeapons, 110, 54, GetString("keyname",GetInt("key","next_weapon")))
	menuControlsNextWeaponButton = GUI_CreateButton(menuControlsWeapons, 10, 50, 90, 20, GetString("tabControls","nextWeapon"))
	menuControlsPreviousWeaponLabel = GUI_CreateLabel(menuControlsWeapons, 110, 84, GetString("keyname",GetInt("key","previous_weapon")))
	menuControlsPreviousWeaponButton = GUI_CreateButton(menuControlsWeapons, 10, 80, 90, 20, GetString("tabControls","prevWeapon"))
	menuControlsSingleplayer = GUI_CreateGroupBox(menuTabControls, 10, 200, 190, 60, GetString("tabControls","singlePlayer"))
	menuControlsFastSaveLabel = GUI_CreateLabel(menuControlsSingleplayer, 110, 24, GetString("keyname",GetInt("key","fast_save")))
	menuControlsFastSaveButton = GUI_CreateButton(menuControlsSingleplayer, 10, 20, 90, 20, GetString("tabControls","fastSave"))
	menuControlsMultiplayer = GUI_CreateGroupBox(menuTabControls, 220, 200, 190, 60, GetString("tabControls","multiPlayer"))
	menuControlsChatLabel = GUI_CreateLabel(menuControlsMultiplayer, 110, 24, GetString("keyname",GetInt("key","chat")))
	menuControlsChatButton = GUI_CreateButton(menuControlsMultiplayer, 10, 20, 90, 20, GetString("tabControls","chat"))
	menuControlsSpecial = GUI_CreateGroupBox(menuTabControls, 430, 140, 190, 120, GetString("tabControls","special"))
	menuControlsFartLabel = GUI_CreateLabel(menuControlsSpecial, 110, 24, GetString("keyname",GetInt("key","fart")))
	menuControlsFartButton = GUI_CreateButton(menuControlsSpecial, 10, 20, 90, 20, GetString("tabControls","fart"))
	menuControlsMobLabel = GUI_CreateLabel(menuControlsSpecial, 110, 54, GetString("keyname",GetInt("key","mob")))
	menuControlsMobButton = GUI_CreateButton(menuControlsSpecial, 10, 50, 90, 20, GetString("tabControls","mob"))
	menuControlsPowerUpLabel = GUI_CreateLabel(menuControlsSpecial, 110, 84, GetString("keyname",GetInt("key","power_up")))
	menuControlsPowerUpButton = GUI_CreateButton(menuControlsSpecial, 10, 80, 90, 20, GetString("tabControls","powerUp"))
	
	menuTabGameplay = GUI_CreateTabPage(menuTab, GetString("tabGameplay","tabTitle"))
	
	menuTabSound = GUI_CreateTabPage(menuTab, GetString("tabSound","tabTitle"))
	menuSoundVolume = GUI_CreateGroupBox(menuTabSound, 10, 10, 480, 220, GetString("tabSound","volume"))
	GUI_CreateLabel(menuSoundVolume,15,20,GetString("tabSound","environment"))
	volumeAmbient = GUI_CreateSlider(menuSoundVolume,130,18,170,GetFloat("sound","volumeAmbient"),0,1)
	GUI_CreateLabel(menuSoundVolume,120,38,GetString("tabSound","mute"))
	GUI_CreateLabel(menuSoundVolume,285,38,GetString("tabSound","loud"))
	menuSoundVolumeAmbient = GUI_CreateButton(menuSoundVolume,340,15,100,20,GetString("tabSound","playTestSound"))
	GUI_CreateLabel(menuSoundVolume,15,70,GetString("tabSound","speech"))
	volumeSpeech = GUI_CreateSlider(menuSoundVolume,130,68,170,GetFloat("sound","volumeSpeech"),0,1)
	GUI_CreateLabel(menuSoundVolume,120,88,GetString("tabSound","mute"))
	GUI_CreateLabel(menuSoundVolume,285,88,GetString("tabSound","loud"))
	menuSoundVolumeSpeech = GUI_CreateButton(menuSoundVolume,340,65,100,20,GetString("tabSound","playTestSound"))
	GUI_CreateLabel(menuSoundVolume,15,120,GetString("tabSound","effects"))
	volumeEffects = GUI_CreateSlider(menuSoundVolume,130,118,170,GetFloat("sound","volumeEffects"),0,1)
	GUI_CreateLabel(menuSoundVolume,120,138,GetString("tabSound","mute"))
	GUI_CreateLabel(menuSoundVolume,285,138,GetString("tabSound","loud"))
	menuSoundVolumeEffects = GUI_CreateButton(menuSoundVolume,340,115,100,20,GetString("tabSound","playTestSound"))
	GUI_CreateLabel(menuSoundVolume,15,170,GetString("tabSound","music"))
	volumeMusic = GUI_CreateSlider(menuSoundVolume,130,168,170,GetFloat("sound","volumeMusic"),0,1)
	GUI_CreateLabel(menuSoundVolume,120,188,GetString("tabSound","mute"))
	GUI_CreateLabel(menuSoundVolume,285,188,GetString("tabSound","loud"))
	menuSoundVolumeMusic = GUI_CreateButton(menuSoundVolume,340,165,100,20,GetString("tabSound","playTestSound"))
	menuSoundRadio = GUI_CreateGroupBox(menuTabSound, 520, 10, 110, 220, GetString("tabSound","radio"))
	menuSoundRadioOn = GUI_CreateRadio(menuSoundRadio, 20, 30, GetString("tabSound","on"), 0)
	menuSoundRadioOff = GUI_CreateRadio(menuSoundRadio, 20, 50, GetString("tabSound","off"), 0)
	
	menuTabLanguage = GUI_CreateTabPage(menuTab, GetString("tabLanguage","tabTitle"))
	menuLanguageBritishEnglish = GUI_CreateImage(menuTabLanguage, 40, 20, 80, 40,LANGUAGE_PATH+"flags/United_Kingdom.png")
	menuLanguageBritishEnglishLabel = GUI_CreateRadio(menuTabLanguage, 40, 70, "British English")
	If(languageSelected = "en") Then GUI_Message(menuLanguageBritishEnglishLabel, "setchecked")
	menuLanguageAmericanEnglish = GUI_CreateImage(menuTabLanguage, 200, 20, 80, 40,LANGUAGE_PATH+"flags/United_States_Of_America.png")
	menuLanguageAmericanEnglishLabel = GUI_CreateRadio(menuTabLanguage, 200, 70, "American English")
	;If(GetString("language","dir") = "en") Then GUI_Message(menuLanguageBritishEnglishLabel, "setchecked")
	menuLanguageGerman = GUI_CreateImage(menuTabLanguage, 360, 20, 80, 40,LANGUAGE_PATH+"flags/Germany.png")
	menuLanguageGermanLabel = GUI_CreateRadio(menuTabLanguage, 360, 70, "Deutsch")
	If(languageSelected = "de") Then GUI_Message(menuLanguageGermanLabel, "setchecked")
	menuLanguageFrench = GUI_CreateImage(menuTabLanguage, 520, 20, 80, 40,LANGUAGE_PATH+"flags/France.png")
	menuLanguageFrenchLabel = GUI_CreateRadio(menuTabLanguage, 520, 70, "Français")
	If(languageSelected = "fr") Then GUI_Message(menuLanguageFrenchLabel, "setchecked")
	menuLanguageSpanish = GUI_CreateImage(menuTabLanguage, 40, 110, 80, 40,LANGUAGE_PATH+"flags/Spain.png")
	menuLanguageSpanishLabel = GUI_CreateRadio(menuTabLanguage, 40, 160, "Español")
	If(languageSelected = "es") Then GUI_Message(menuLanguageSpanishLabel, "setchecked")
	menuLanguageItalian = GUI_CreateImage(menuTabLanguage, 200, 110, 80, 40,LANGUAGE_PATH+"flags/Italy.png")
	menuLanguageItalianLabel = GUI_CreateRadio(menuTabLanguage, 200, 160, "Italiano")
	If(languageSelected = "it") Then GUI_Message(menuLanguageItalianLabel, "setchecked")
	menuLanguagePortuguese = GUI_CreateImage(menuTabLanguage, 360, 110, 80, 40,LANGUAGE_PATH+"flags/Portugal.png")
	menuLanguagePortugueseLabel = GUI_CreateRadio(menuTabLanguage, 360, 160, "Português")
	If(languageSelected = "pt") Then GUI_Message(menuLanguagePortugueseLabel, "setchecked")
	menuLanguagePolish = GUI_CreateImage(menuTabLanguage, 520, 110, 80, 40,LANGUAGE_PATH+"flags/Poland.png")
	menuLanguagePolishLabel = GUI_CreateRadio(menuTabLanguage, 520, 160, "Polski")
	If(languageSelected = "pl") Then GUI_Message(menuLanguagePolishLabel, "setchecked")
	menuLanguageDanish = GUI_CreateImage(menuTabLanguage, 40, 200, 80, 40,LANGUAGE_PATH+"flags/Denmark.png")
	menuLanguageDanishLabel = GUI_CreateRadio(menuTabLanguage, 40, 250, "Dansk")
	If(languageSelected = "dk") Then GUI_Message(menuLanguageDanishLabel, "setchecked")
	menuLanguageSwedish = GUI_CreateImage(menuTabLanguage, 200, 200, 80, 40,LANGUAGE_PATH+"flags/Sweden.png")
	menuLanguageSwedishLabel = GUI_CreateRadio(menuTabLanguage, 200, 250, "Svenska")
	If(languageSelected = "se") Then GUI_Message(menuLanguageSwedishLabel, "setchecked")
	menuLanguageDutch = GUI_CreateImage(menuTabLanguage, 360, 200, 80, 40,LANGUAGE_PATH+"flags/Netherlands.png")
	menuLanguageDutchLabel = GUI_CreateRadio(menuTabLanguage, 360, 250, "Nederlands")
	If(languageSelected = "nl") Then GUI_Message(menuLanguageDutchLabel, "setchecked")
	menuLanguageTurkish = GUI_CreateImage(menuTabLanguage, 520, 200, 80, 40,LANGUAGE_PATH+"flags/Turkey.png")
	menuLanguageTurkishLabel = GUI_CreateRadio(menuTabLanguage, 520, 250, "Türk")
	If(languageSelected = "tr") Then GUI_Message(menuLanguageTurkishLabel, "setchecked")
	
	menuTabCheats = GUI_CreateTabPage(menuTab, GetString("tabCheats","tabTitle"))
	menuTabCheatsInfo = GUI_CreateLabel(menuTabCheats, 20, 20, GetString("tabCheats","info"))
	menuTabCheatsPlayerModifier = GUI_CreateGroupBox(menuTabCheats, 20, 50, 210, 210, GetString("tabCheats","playerModifier"))
	menuTabCheatsPlayerModifierInvulnerability = GUI_CreateCheckBox(menuTabCheatsPlayerModifier, 20, 30, " "+GetString("tabCheats","invulnerability"))
	menuTabCheatsPlayerModifierArmor = GUI_CreateCheckBox(menuTabCheatsPlayerModifier, 20, 55, " "+GetString("tabCheats","armor"))
	menuTabCheatsPlayerModifierAllWeapons = GUI_CreateCheckBox(menuTabCheatsPlayerModifier, 20, 80, " "+GetString("tabCheats","allWeapons"))
	menuTabCheatsPlayerModifierInfinityAmmo = GUI_CreateCheckBox(menuTabCheatsPlayerModifier, 20, 105, " "+GetString("tabCheats","infinityAmmo"))
	menuTabCheatsPlayerModifierCarInvulnerable = GUI_CreateCheckBox(menuTabCheatsPlayerModifier, 20, 130, " "+GetString("tabCheats","carInvulnerable"))
	menuTabCheatsPlayerModifierCarDoorsLocked = GUI_CreateCheckBox(menuTabCheatsPlayerModifier, 20, 155, " "+GetString("tabCheats","carDoorsLocked"))
	
	menuTabEditor = GUI_CreateTabPage(menuTab, GetString("tabEditor","tabTitle"))
	menuTabEditorControls = GUI_CreateGroupBox(menuTabEditor, 10, 10, 180, 210, GetString("tabEditor","controls"))
	menuTabEditorControlsForwardLabel = GUI_CreateLabel(menuTabEditorControls, 100, 24, GetString("keyname",GetInt("key","moveUp")))
	menuTabEditorControlsForwardButton = GUI_CreateButton(menuTabEditorControls, 10, 20, 80, 20, GetString("tabEditor","moveUp"))
	menuTabEditorControlsBackwardLabel = GUI_CreateLabel(menuTabEditorControls, 100, 54, GetString("keyname",GetInt("key","moveDown")))
	menuTabEditorControlsBackwardButton = GUI_CreateButton(menuTabEditorControls, 10, 50, 80, 20, GetString("tabEditor","moveDown"))
	menuTabEditorControlsLeftLabel = GUI_CreateLabel(menuTabEditorControls, 100, 84, GetString("keyname",GetInt("key","moveLeft")))
	menuTabEditorControlsLeftButton = GUI_CreateButton(menuTabEditorControls, 10, 80, 80, 20, GetString("tabEditor","moveLeft"))
	menuTabEditorControlsRightLabel = GUI_CreateLabel(menuTabEditorControls, 100, 114, GetString("keyname",GetInt("key","moveRight")))
	menuTabEditorControlsRightButton = GUI_CreateButton(menuTabEditorControls, 10, 110, 80, 20, GetString("tabEditor","moveRight"))
	menuTabEditorControlsFastLabel = GUI_CreateLabel(menuTabEditorControls, 100, 144, GetString("keyname",GetInt("key","moveFast")))
	menuTabEditorControlsFastButton = GUI_CreateButton(menuTabEditorControls, 10, 140, 80, 20, GetString("tabEditor","moveFast"))
	menuTabEditorControlsPlaceManyLabel = GUI_CreateLabel(menuTabEditorControls, 100, 174, GetString("keyname",GetInt("key","placeMany")))
	menuTabEditorControlsPlaceManyButton = GUI_CreateButton(menuTabEditorControls, 10, 170, 80, 20, GetString("tabEditor","placeMany"))
	menuTabEditorHowToUse = GUI_CreateGroupBox(menuTabEditor, 210, 10, 380, 250, GetString("tabEditor","howToUse"))
	menuTabEditorHowTo1 = GUI_CreateLabel(menuTabEditorHowToUse, 20, 20, GetString("tabEditor","howTo1"))
	menuTabEditorHowTo2 = GUI_CreateLabel(menuTabEditorHowToUse, 20, 44, GetString("tabEditor","howTo2"))
	menuTabEditorHowTo3 = GUI_CreateLabel(menuTabEditorHowToUse, 20, 60, GetString("tabEditor","howTo3"))
	menuTabEditorHowTo4 = GUI_CreateLabel(menuTabEditorHowToUse, 20, 85, GetString("tabEditor","howTo4"))
	menuTabEditorHowTo5 = GUI_CreateLabel(menuTabEditorHowToUse, 20, 110, GetString("tabEditor","howTo5"))
	menuTabEditorHowTo6 = GUI_CreateLabel(menuTabEditorHowToUse, 20, 135, GetString("tabEditor","howTo6"))
	menuTabEditorHowTo7 = GUI_CreateLabel(menuTabEditorHowToUse, 20, 150, GetString("tabEditor","howTo7"))
	menuTabEditorHowTo8 = GUI_CreateLabel(menuTabEditorHowToUse, 20, 175, GetString("tabEditor","howTo8"))
	menuTabEditorHowTo9 = GUI_CreateLabel(menuTabEditorHowToUse, 20, 200, GetString("tabEditor","howTo9"))
	menuTabEditorHowTo10 = GUI_CreateLabel(menuTabEditorHowToUse, 20, 225, GetString("tabEditor","howTo10"))
	menuTabEditorStart = GUI_CreateButton(menuTabEditor, 20, 230, 160, 20, GetString("tabEditor","start"))
	
	menuTabCredits = GUI_CreateTabPage(menuTab, GetString("tabCredits","tabTitle"))
	menuTabCreditsProgramming = GUI_CreateGroupBox(menuTabCredits, 20, 20, 150, 50, GetString("tabCredits","programming"))
	menuTabCreditsProgrammingLabel = GUI_CreateLabel(menuTabCreditsProgramming,20,20,"Stefan Behrendt")
	menuTabCreditsSounds = GUI_CreateGroupBox(menuTabCredits,20,90,150,90,GetString("tabCredits","sounds"))
	menuTabCreditsSoundsLabel1 = GUI_CreateLabel(menuTabCreditsSounds,20,20,"Stefan Behrendt")
	menuTabCreditsSoundsLabel2 = GUI_CreateLabel(menuTabCreditsSounds,20,40,"www.freesound.org")
	menuTabCreditsSoundsLabel3 = GUI_CreateLabel(menuTabCreditsSounds,20,60,"www.soundbible.com")
	menuTabCreditsMusic = GUI_CreateGroupBox(menuTabCredits,20,200,150,50,GetString("tabCredits","music"))
	menuTabCreditsMusicLabel = GUI_CreateLabel(menuTabCreditsMusic,20,20,"Stefan Behrendt")
	menuTabCreditsGraphics = GUI_CreateGroupBox(menuTabCredits, 200, 20, 250, 90, GetString("tabCredits","graphics"))
	menuTabCreditsGraphicsLabel1= GUI_CreateLabel(menuTabCreditsGraphics,20,20,"Stefan Behrendt")
	menuTabCreditsGraphicsLabel2= GUI_CreateLabel(menuTabCreditsGraphics,20,40,"Devils Child (GUI & Particle Design)")
	menuTabCreditsGraphicsLabel3= GUI_CreateLabel(menuTabCreditsGraphics,20,60,"DrFreak339 (GUI 'BackRow' Design)")
	menuTabCreditsTools = GUI_CreateGroupBox(menuTabCredits, 200, 120, 250, 150, GetString("tabCredits","tools"))
	menuTabCreditsToolsLabel1 = GUI_CreateLabel(menuTabCreditsTools,20,20,"Devil GUI")
	menuTabCreditsToolsLabel2 = GUI_CreateLabel(menuTabCreditsTools,20,40,"Devil Particle System")
	menuTabCreditsToolsLabel3 = GUI_CreateLabel(menuTabCreditsTools,20,60,"Cubase, Audacity (Sound & Music)")
	menuTabCreditsToolsLabel4 = GUI_CreateLabel(menuTabCreditsTools,20,80,"GIMP (Graphics)")
	menuTabCreditsToolsLabel5 = GUI_CreateLabel(menuTabCreditsTools,20,100,"MilkShape3D (3D Models)")
	menuTabCreditsToolsLabel6 = GUI_CreateLabel(menuTabCreditsTools,20,120,"MakeHuman (Logo Design)")
	menuTabCreditsSpecialThanks = GUI_CreateGroupBox(menuTabCredits, 480, 20, 150, 250, GetString("tabCredits","specialThanks"))
	menuTabCreditsSpecialThanksLabel1 = GUI_CreateLabel(menuTabCreditsSpecialThanks,10,20,"Mark Sibly for Blitz3D")
	menuTabCreditsSpecialThanksLabel2 = GUI_CreateLabel(menuTabCreditsSpecialThanks,10,50,"Gang of the Coconuts")
	menuTabCreditsSpecialThanksLabel3 = GUI_CreateLabel(menuTabCreditsSpecialThanks,10,65,"  for Free Country Flags")
	menuTabCreditsSpecialThanksLabel4 = GUI_CreateLabel(menuTabCreditsSpecialThanks,10,95,"Google Image Search")
	menuTabCreditsSpecialThanksLabel5 = GUI_CreateLabel(menuTabCreditsSpecialThanks,10,110,"  for nice inspirations ;)")
	
	GUI_Message(menuTab, "setindex", 1)
	
	menuSinglePlayer = GUI_CreateGroupBox(menuWindow, 20, 330, 250, 120, GetString("singlePlayer","tabTitle"))
	menuSinglePlayerMapsLabel = GUI_CreateLabel(menuSinglePlayer, 60, 22, GetString("singlePlayer","selectMap"), "center")
	menuSinglePlayerMaps = GUI_CreateListBox(menuSinglePlayer, 20, 40, 100, 60)
	Local dir = ReadDir("maps")
	Repeat
		Local currentFile$ = NextFile$(dir)
		If currentFile = "" Then Exit 
		If(Right(currentFile,3) = "map") Then
			GUI_Message(menuSinglePlayerMaps, "additem", -1, Left(currentFile,Len(currentFile)-4))
		EndIf
	Forever 
	CloseDir(dir)
	menuSinglePlayerStartNewGame = GUI_CreateButton(menuSinglePlayer, 130, 47, 100, 20, GetString("singlePlayer","startNewGame"))
	menuSinglePlayerLoadSaveGame = GUI_CreateButton(menuSinglePlayer, 130, 73, 100, 20, GetString("singlePlayer","loadSavedGame"))
	
	menuLocalNetwork = GUI_CreateGroupBox(menuWindow, 280, 330, 250, 120, GetString("localNetwork","tabTitle"))
	menuLocalNetworkHostMapsLabel = GUI_CreateLabel(menuLocalNetwork, 60, 22, GetString("localNetwork","hostMap"), "center")
	menuLocalNetworkHostMaps = GUI_CreateListBox(menuLocalNetwork, 20, 40, 100, 60)
	dir = ReadDir("maps/multiplayer")
	;change: only read multiplayer maps
	Repeat
		currentFile$ = NextFile$(dir)
		If currentFile = "" Then Exit 
		If(Right(currentFile,3) = "map") Then
			GUI_Message(menuLocalNetworkHostMaps, "additem", -1, Left(currentFile,Len(currentFile)-4))
		EndIf
	Forever 
	CloseDir(dir)
	menuLocalNetworkHostGame = GUI_CreateButton(menuLocalNetwork, 130, 47, 100, 20, GetString("localNetwork","hostGame"), "")
	menuLocalNetworkJoinGame = GUI_CreateButton(menuLocalNetwork, 130, 73, 100, 20, GetString("localNetwork","joinGame"), "")
	
	menuInternet = GUI_CreateGroupBox(menuWindow, 540, 330, 140, 120, GetString("internet","tabTitle"))
End Function

Function MenuEvents()
	Select GUI_AppEvent()
		Case menuSinglePlayerStartNewGame
			GUI_Message(menuWindow, "minimize")
			map = GUI_Message(menuSinglePlayerMaps, "gettext")
			SaveMenuSettings()
			section = "init single player"
		Case menuControlsForwardButton
			GUI_Message(menuControlsForwardLabel, "settext", GetUserKey())
		Case menuControlsBackwardButton
			GUI_Message(menuControlsBackwardLabel, "settext", GetUserKey())
		Case menuControlsLeftButton
			GUI_Message(menuControlsLeftLabel, "settext", GetUserKey())	
		Case menuControlsRightButton
			GUI_Message(menuControlsRightLabel, "settext", GetUserKey())
		Case menuControlsJumpButton
			GUI_Message(menuControlsJumpLabel, "settext", GetUserKey())
		Case menuControlsEnterButton
			GUI_Message(menuControlsEnterLabel, "settext", GetUserKey())
		Case menuControlsHandBrakeButton
			GUI_Message(menuControlsHandBrakeLabel, "settext", GetUserKey())
		Case menuControlsHornButton
			GUI_Message(menuControlsHornLabel, "settext", GetUserKey())
		Case menuControlsLightButton
			GUI_Message(menuControlsLightLabel, "settext", GetUserKey())
		Case menuControlsRadioButton
			GUI_Message(menuControlsRadioLabel, "settext", GetUserKey())
		Case menuControlsAttackButton
			GUI_Message(menuControlsAttackLabel, "settext", GetUserKey())
		Case menuControlsNextWeaponButton
			GUI_Message(menuControlsNextWeaponLabel, "settext", GetUserKey())
		Case menuControlsPreviousWeaponButton
			GUI_Message(menuControlsPreviousWeaponLabel, "settext", GetUserKey())
		Case menuControlsFastSaveButton
			GUI_Message(menuControlsFastSaveLabel, "settext", GetUserKey())
		Case menuControlsChatButton
			GUI_Message(menuControlsChatLabel, "settext", GetUserKey())
		Case menuControlsFartButton
			GUI_Message(menuControlsFartLabel, "settext", GetUserKey())	
		Case menuControlsMobButton
			GUI_Message(menuControlsMobLabel, "settext", GetUserKey())
		Case menuControlsPowerUpButton
			GUI_Message(menuControlsPowerUpLabel, "settext", GetUserKey())
		Case menuSoundVolumeAmbient
			channelAmbient = PlaySound(testsound(0))
			ChannelVolume(channelAmbient,GUI_Message(volumeAmbient,"getvalue"))
		Case menuSoundVolumeSpeech
			channelSpeech = PlaySound(testsound(1))
			ChannelVolume(channelSpeech,GUI_Message(volumeSpeech,"getvalue"))
		Case menuSoundVolumeEffects
			channelEffects = PlaySound(testsound(2))
			ChannelVolume(channelEffects,GUI_Message(volumeEffects,"getvalue"))
		Case menuSoundVolumeMusic
			channelMusic = PlaySound(testsound(3))
			ChannelVolume(channelMusic,GUI_Message(volumeMusic,"getvalue"))
		Case menuLanguageBritishEnglishLabel, menuLanguageAmericanEnglishLabel
			languageSelected="en"
			SaveMenuSettings()
			InitMenu()
		Case menuLanguageGermanLabel
			languageSelected="de"
			SaveMenuSettings()
			InitMenu()
		Case menuLanguageFrenchLabel
			languageSelected="fr"
			SaveMenuSettings()
			InitMenu()
		Case menuLanguageSpanishLabel
			languageSelected="es"
			SaveMenuSettings()
			InitMenu()
		Case menuLanguageItalianLabel
			languageSelected="it"
			SaveMenuSettings()
			InitMenu()
		Case menuLanguagePortugueseLabel
			languageSelected="pt"
			SaveMenuSettings()
			InitMenu()
		Case menuLanguagePolishLabel
			languageSelected="pl"
			SaveMenuSettings()
			InitMenu()
		Case menuLanguageDanishLabel
			languageSelected="dk"
			SaveMenuSettings()
			InitMenu()
		Case menuLanguageSwedishLabel
			languageSelected="se"
			SaveMenuSettings()
			InitMenu()
		Case menuLanguageDutchLabel
			languageSelected="nl"
			SaveMenuSettings()
			InitMenu()
		Case menuLanguageTurkishLabel
			languageSelected="tr"
			SaveMenuSettings()
			InitMenu()
		Case menuTabEditorControlsForwardButton
			GUI_Message(menuTabEditorControlsForwardLabel, "settext", GetUserKey())
		Case menuTabEditorControlsBackwardButton
			GUI_Message(menuTabEditorControlsBackwardLabel, "settext", GetUserKey())
		Case menuTabEditorControlsLeftButton
			GUI_Message(menuTabEditorControlsLeftLabel, "settext", GetUserKey())
		Case menuTabEditorControlsRightButton
			GUI_Message(menuTabEditorControlsRightLabel, "settext", GetUserKey())
		Case menuTabEditorControlsFastButton
			GUI_Message(menuTabEditorControlsFastLabel, "settext", GetUserKey())
		Case menuTabEditorControlsPlaceManyButton
			GUI_Message(menuTabEditorControlsPlaceManyLabel, "settext", GetUserKey())
		Case menuTabEditorStart
			SaveMenuSettings()
			Delete Each Behaviour
			InitGame()
			section = "init editor"
		Case menuLocalNetworkHostGame
			;connect via UDP
			udpInputStream = CreateUDPStream(UDP_INPUT_PORT)
			udpOutputStream = CreateUDPStream(UDP_OUTPUT_PORT)
			
			;if connection failed - abort
			If(udpInputStream=0 Or udpOutputStream=0) Then
				CloseUDPStream(udpInputStream)
				CloseUDPStream(udpOutputStream)
				GUI_MsgBox(GetString("localNetwork","hostConnectionFailureTitle"), GetString("localNetwork","hostConnectionFailure"))
			Else
				;create window while waiting for clients
				localPlayerNumber = 1
				DebugLog("I am number "+localPlayerNumber)
				
				hostWaitForClientsWindow = GUI_CreateWindow(-1, -1, 300, 300, GetString("localNetwork","hostWaitForClients"), "", False, False, False)
				Local infoLabel1 = GUI_CreateLabel(hostWaitForClientsWindow, 150, 20, GetString("localNetwork","infoLabel1") + " " + LOCALHOST_IP_ADDRESS, "center")
				Local infoLabel2 = GUI_CreateLabel(hostWaitForClientsWindow, 150, 40, GetString("localNetwork","infoLabel2"), "center")
				Local clientListGroup = GUI_CreateGroupBox(hostWaitForClientsWindow, 20, 70, 260, 150, GetString("localNetwork","clientList"))
				Local startGameButton = GUI_CreateButton(hostWaitForClientsWindow, 30, 230, 110, 20, GetString("localNetwork", "startHostGame"))
				Local cancelHostButton = GUI_CreateButton(hostWaitForClientsWindow, 160, 230, 110, 20, GetString("localNetwork", "cancelHost"))
				Local client1 = GUI_CreateLabel(clientListGroup, 20, 20, "1) <no client>")
				Local client2 = GUI_CreateLabel(clientListGroup, 20, 40, "2) <no client>")
				Local client3 = GUI_CreateLabel(clientListGroup, 20, 60, "3) <no client>")
				Local client4 = GUI_CreateLabel(clientListGroup, 20, 80, "4) <no client>")
				Local client5 = GUI_CreateLabel(clientListGroup, 20, 100, "5) <no client>")
				Local client6 = GUI_CreateLabel(clientListGroup, 20, 120, "6) <no client>")
				GUI_Message(hostWaitForClientsWindow, "setlocked", True)
				GUI_Message(menuLocalNetworkHostGame, "setenabled", False)
				
				Repeat
					Cls()
					;bring this window to front
					GUI_Message(hostWaitForClientsWindow, "bringtofront")
					
					;wait for clients
					Local clientIP = RecvUDPMsg(udpInputStream)
					If(clientIP <> 0) Then
						If(ReadAvail(udpInputStream) > 0) Then
							Local checkByte = ReadByte(udpInputStream)
							;new client
							If(checkByte = 1) Then
								;update client list
								If(GUI_Message(client1,"gettext") = "1) <no client>") Then
									GUI_Message(client1,"settext","1) "+DottedIP(clientIP))
									WriteByte(udpOutputStream,11)	; 11-16 = "hi client, you are number [1;6]."
								ElseIf(GUI_Message(client2,"gettext") = "2) <no client>") Then
									GUI_Message(client2,"settext","2) "+DottedIP(clientIP))
									WriteByte(udpOutputStream,12)	; 11-16 = "hi client, you are number [1;6]."
								ElseIf(GUI_Message(client3,"gettext") = "3) <no client>") Then
									GUI_Message(client3,"settext","3) "+DottedIP(clientIP))
									WriteByte(udpOutputStream,13)	; 11-16 = "hi client, you are number [1;6]."
								ElseIf(GUI_Message(client4,"gettext") = "4) <no client>") Then
									GUI_Message(client4,"settext","4) "+DottedIP(clientIP))
									WriteByte(udpOutputStream,14)	; 11-16 = "hi client, you are number [1;6]."
								ElseIf(GUI_Message(client5,"gettext") = "5) <no client>") Then
									GUI_Message(client5,"settext","5) "+DottedIP(clientIP))
									WriteByte(udpOutputStream,15)	; 11-16 = "hi client, you are number [1;6]."
								ElseIf(GUI_Message(client6,"gettext") = "6) <no client>") Then
									GUI_Message(client6,"settext","6) "+DottedIP(clientIP))
									WriteByte(udpOutputStream,16)	; 11-16 = "hi client, you are number [1;6]."
								EndIf
								
								;send answer
								SendUDPMsg(udpOutputStream,clientIP,UDP_INPUT_PORT)
							ElseIf(checkByte = 4) Then
								;refuse client
								If(GUI_Message(client1,"gettext") = "1) "+DottedIP(clientIP)) Then
									GUI_Message(client1,"settext","1) <no client>")
								ElseIf(GUI_Message(client2,"gettext") = "2) "+DottedIP(clientIP)) Then
									GUI_Message(client2,"settext","2) <no client>")
								ElseIf(GUI_Message(client3,"gettext") = "3) "+DottedIP(clientIP)) Then
									GUI_Message(client3,"settext","3) <no client>")
								ElseIf(GUI_Message(client4,"gettext") = "4) "+DottedIP(clientIP)) Then
									GUI_Message(client4,"settext","4) <no client>")
								ElseIf(GUI_Message(client5,"gettext") = "5) "+DottedIP(clientIP)) Then
									GUI_Message(client5,"settext","5) <no client>")
								ElseIf(GUI_Message(client6,"gettext") = "6) "+DottedIP(clientIP)) Then
									GUI_Message(client6,"settext","6) <no client>")
								EndIf
							EndIf
						EndIf
					EndIf
					
					;start game button
					If(GUI_AppEvent() = startGameButton) Then
						Local enoughClients = False
						
						;send every client the message to start the game
						Local currentClientIP$ = Mid(GUI_Message(client1,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,3)	; 3 = lets start the game
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
							enoughClients = True
						EndIf
						currentClientIP$ = Mid(GUI_Message(client2,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,3)	; 3 = lets start the game
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
							enoughClients = True
						EndIf
						currentClientIP$ = Mid(GUI_Message(client3,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,3)	; 3 = lets start the game
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
							enoughClients = True
						EndIf
						currentClientIP$ = Mid(GUI_Message(client4,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,3)	; 3 = lets start the game
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
							enoughClients = True
						EndIf
						currentClientIP$ = Mid(GUI_Message(client5,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,3)	; 3 = lets start the game
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
							enoughClients = True
						EndIf
						currentClientIP$ = Mid(GUI_Message(client6,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,3)	; 3 = lets start the game
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
							enoughClients = True
						EndIf
						
						;and start the network game! yeah!!!
						If(enoughClients) Then
							GUI_Message(hostWaitForClientsWindow, "close")
							GUI_Message(menuWindow, "minimize")
							map = GUI_Message(menuSinglePlayerMaps, "gettext")
							SaveMenuSettings()
							localPlayerNumber = 1
							map = "multiplayer/" + GUI_Message(menuLocalNetworkHostMaps, "gettext")
							section = "init local network"
							Return
						Else
							GUI_MsgBox(GetString("localNetwork","notEnoughClientsTitle"), GetString("localNetwork","notEnoughClients"))
						EndIf
					EndIf
					
					;cancel button
					If(GUI_AppEvent() = cancelHostButton) Then
						;inform all clients that this host is canceled
						currentClientIP$ = Mid(GUI_Message(client1,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,4)	; 4 = refuse connection
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
						EndIf
						currentClientIP$ = Mid(GUI_Message(client2,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,4)	; 4 = refuse connection
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
						EndIf
						currentClientIP$ = Mid(GUI_Message(client3,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,4)	; 4 = refuse connection
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
						EndIf
						currentClientIP$ = Mid(GUI_Message(client4,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,4)	; 4 = refuse connection
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
						EndIf
						currentClientIP$ = Mid(GUI_Message(client5,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,4)	; 4 = refuse connection
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
						EndIf
						currentClientIP$ = Mid(GUI_Message(client6,"gettext"),4)
						If(currentClientIP$ <> "<no client>") Then
							WriteByte(udpOutputStream,4)	; 4 = refuse connection
							SendUDPMsg(udpOutputStream,Int_IP(currentClientIP$),UDP_INPUT_PORT)
						EndIf
						
						CloseUDPStream(udpInputStream)
						CloseUDPStream(udpOutputStream)
						GUI_Message(hostWaitForClientsWindow, "close")
						GUI_Message(menuLocalNetworkHostGame, "setenabled", True)
						GUI_Message(menuWindow, "bringtofront")
						Return
					EndIf
					
					GUI_UpdateGUI()
					Flip(vsync)
				Forever
			EndIf
		Case menuLocalNetworkJoinGame
			Local joinWindow = GUI_CreateWindow(-1, -1, 200, 150, GetString("localNetwork","enterHostIPTitle"), "", False, False, False)
			Local joinLabel = GUI_CreateLabel(joinWindow, 100, 10, GetString("localNetwork","enterHostIP"), "center")
			Local ipEdit = GUI_CreateEdit(joinWindow, 50, 40, 100, "192.168.xxx.xxx")
			Local tryConnectButton = GUI_CreateButton(joinWindow, 10, 70, 100, 20, GetString("localNetwork","tryConnect"))
			Local cancelJoinButton = GUI_CreateButton(joinWindow, 120, 70, 70, 20, GetString("localNetwork","cancelJoin"))
			GUI_Message(joinWindow, "setlocked", True)
			GUI_Message(menuLocalNetworkJoinGame, "setenabled", False)
			
			Local hostHasAccepted = False
			
			Repeat
				Cls()
				;bring this window to front
				GUI_Message(joinWindow, "bringtofront")
				
				;try connect button
				If(GUI_AppEvent() = tryConnectButton) Then
					udpInputStream = CreateUDPStream(UDP_INPUT_PORT)
					udpOutputStream = CreateUDPStream(UDP_OUTPUT_PORT)
					
					;if connection failed - abort
					If(udpInputStream=0 Or udpOutputStream=0) Then
						CloseUDPStream(udpInputStream)
						CloseUDPStream(udpOutputStream)
						GUI_MsgBox(GetString("localNetwork","connectionFailureTitle"), GetString("localNetwork","connectionFailure"))
						GUI_Message(joinWindow, "close")
						GUI_Message(menuLocalNetworkJoinGame, "setenabled", True)
						GUI_Message(menuWindow, "bringtofront")
						Return
					Else
						;send message to host
						WriteByte(udpOutputStream,1)	; 1 = hi, im a client, please add me to your client list.
						SendUDPMsg(udpOutputStream,Int_IP(GUI_Message(ipEdit, "gettext")),UDP_INPUT_PORT)
						udpTimeCounter = MilliSecs()
						
						;show wait window
						Local waitWindow = GUI_CreateWindow(-1, -1, 250, 150, GetString("localNetwork","tryConnect"), "", False, False, False)
						GUI_Message(waitWindow, "setlocked", True)
						
						;wait for answer
						Repeat
							Cls()
							GUI_Message(waitWindow, "bringtofront")
							
							;timeout?
							If(Not(hostHasAccepted)) Then
								If(udpTimeCounter+UDP_TIMEOUT < MilliSecs()) Then
									Local timeoutInfo = GUI_MsgBox(GetString("localNetwork","connectionFailureTitle"), GetString("localNetwork","timeout"))
									;inform host about refusion
									WriteByte(udpOutputStream,4)	; 4 = "sorry, i refuse being a client"
									SendUDPMsg(udpOutputStream,Int_IP(GUI_Message(ipEdit, "gettext")),UDP_INPUT_PORT)
									
									;reset stuff
									CloseUDPStream(udpInputStream)
									CloseUDPStream(udpOutputStream)
									GUI_Message(waitWindow, "close")
									GUI_Message(joinWindow, "close")
									GUI_Message(menuLocalNetworkJoinGame, "setenabled", True)
									GUI_Message(menuWindow, "bringtofront")
									Return
								EndIf
							EndIf
							
							Local message = RecvUDPMsg(udpInputStream)
							DebugLog(message)
							
							If(message <> 0) Then
								If(ReadAvail(udpInputStream) > 0) Then
									checkByte = ReadByte(udpInputStream)
									
									Select checkByte
										Case 11,12,13,14,15,16
											;host has accepted
											localPlayerNumber = checkByte-9
											DebugLog("I am number "+localPlayerNumber)
											Local hasAccepted1 = GUI_CreateLabel(waitWindow, 125, 20, GetString("localNetwork","hasConnected1"), "center")
											Local hasAccepted2 = GUI_CreateLabel(waitWindow, 125, 40, GetString("localNetwork","hasConnected2"), "center")
											refuseButton = GUI_CreateButton(waitWindow, 90, 70, 70, 20, GetString("localNetwork","cancelJoin"))
											hostHasAccepted = True
											
										Case 3
											;start game
											GUI_Message(waitWindow, "close")
											GUI_Message(joinWindow, "close")
											GUI_Message(menuWindow, "minimize")
											map = GUI_Message(menuSinglePlayerMaps, "gettext")
											SaveMenuSettings()
											map = "multiplayer/" + GUI_Message(menuLocalNetworkHostMaps, "gettext")
											section = "init local network"
											Return
											
										Case 4
										;refuse connection
											CloseUDPStream(udpInputStream)
											CloseUDPStream(udpOutputStream)
											GUI_Message(waitWindow, "close")
											GUI_Message(joinWindow, "close")
											GUI_Message(menuLocalNetworkJoinGame, "setenabled", True)
											GUI_Message(menuWindow, "bringtofront")
											GUI_MsgBox(GetString("localNetwork","hostRefusedTitle"), GetString("localNetwork","hostRefused"))
											Return
									End Select
								EndIf
							EndIf
							
							;refuse join
							If(GUI_AppEvent() = refuseButton) Then
								;inform host about refusion
								WriteByte(udpOutputStream,4)	; 4 = "sorry, i refuse being a client"
								SendUDPMsg(udpOutputStream,Int_IP(GUI_Message(ipEdit, "gettext")),UDP_INPUT_PORT)
								
								;reset stuff
								CloseUDPStream(udpInputStream)
								CloseUDPStream(udpOutputStream)
								GUI_Message(waitWindow, "close")
								GUI_Message(joinWindow, "close")
								GUI_Message(menuLocalNetworkJoinGame, "setenabled", True)
								GUI_Message(menuWindow, "bringtofront")
								Return
							EndIf
							
							GUI_UpdateGUI()
							Flip(vsync)
						Forever
					EndIf
				EndIf
				
				;cancel button
				If(GUI_AppEvent() = cancelJoinButton) Then
					GUI_Message(joinWindow, "close")
					GUI_Message(menuLocalNetworkJoinGame, "setenabled", True)
					GUI_Message(menuWindow, "bringtofront")
					Return
				EndIf
				
				GUI_UpdateGUI()
				Flip(vsync)
			Forever
	End Select
End Function

Function SaveMenuSettings()
	;graphics
	fileStream = WriteFile("game.ini")
	WriteLine(fileStream,"[graphics]")
	Local res$ = GUI_Message(menuScreenResolution, "gettext")
	Local delimiter = Instr(res,"x")
	WriteLine(fileStream,"screenWidth="+Left(res,delimiter-2))
	WriteLine(fileStream,"screenHeight="+Mid(res,delimiter+2))
	Local screenMode = GUI_Message(menuScreenWindowed, "getchecked")
	screenMode=screenMode+1
	WriteLine(fileStream,"screenMode="+screenMode)
	WriteLine(fileStream,"vsync="+Int(GUI_Message(menuScreenVSync, "getchecked")))
	WriteLine(fileStream,"")
	WriteLine(fileStream,"[sound]")
	WriteLine(fileStream,"volumeAmbient="+GUI_Message(volumeAmbient,"getValue"))
	ChannelVolume(channelAmbient,GUI_Message(volumeAmbient,"getValue"))
	WriteLine(fileStream,"volumeSpeech="+GUI_Message(volumeSpeech,"getValue"))
	ChannelVolume(channelSpeech,GUI_Message(volumeSpeech,"getValue"))
	WriteLine(fileStream,"volumeEffects="+GUI_Message(volumeEffects,"getValue"))
	ChannelVolume(channelEffects,GUI_Message(volumeEffects,"getValue"))
	WriteLine(fileStream,"volumeMusic="+GUI_Message(volumeMusic,"getValue"))
	ChannelVolume(channelMusic,GUI_Message(volumeMusic,"getValue"))
	WriteLine(fileStream,"")
	WriteLine(fileStream,"[language]")
	WriteLine(fileStream,"dir="+languageSelected)
	CloseFile(fileStream)
	
	;keys
	fileStream = WriteFile("keys.ini")
	WriteLine(fileStream,"[key]")
	WriteLine(fileStream,"forward="+FindKeyCode(GUI_Message(menuControlsForwardLabel, "gettext")))
	WriteLine(fileStream,"backward="+FindKeyCode(GUI_Message(menuControlsBackwardLabel, "gettext")))
	WriteLine(fileStream,"left="+FindKeyCode(GUI_Message(menuControlsLeftLabel, "gettext")))
	WriteLine(fileStream,"right="+FindKeyCode(GUI_Message(menuControlsRightLabel, "gettext")))
	WriteLine(fileStream,"jump="+FindKeyCode(GUI_Message(menuControlsJumpLabel, "gettext")))
	WriteLine(fileStream,"enter_vehicle="+FindKeyCode(GUI_Message(menuControlsEnterLabel, "gettext")))
	WriteLine(fileStream,"handbrake="+FindKeyCode(GUI_Message(menuControlsHandBrakeLabel, "gettext")))
	WriteLine(fileStream,"horn="+FindKeyCode(GUI_Message(menuControlsHornLabel, "gettext")))
	WriteLine(fileStream,"light="+FindKeyCode(GUI_Message(menuControlsLightLabel, "gettext")))
	WriteLine(fileStream,"radio="+FindKeyCode(GUI_Message(menuControlsRadioLabel, "gettext")))
	WriteLine(fileStream,"attack="+FindKeyCode(GUI_Message(menuControlsAttackLabel, "gettext")))
	WriteLine(fileStream,"next_weapon="+FindKeyCode(GUI_Message(menuControlsNextWeaponLabel, "gettext")))
	WriteLine(fileStream,"previous_weapon="+FindKeyCode(GUI_Message(menuControlsPreviousWeaponLabel, "gettext")))
	WriteLine(fileStream,"fast_save="+FindKeyCode(GUI_Message(menuControlsFastSaveLabel, "gettext")))
	WriteLine(fileStream,"chat="+FindKeyCode(GUI_Message(menuControlsChatLabel, "gettext")))
	WriteLine(fileStream,"fart="+FindKeyCode(GUI_Message(menuControlsFartLabel, "gettext")))
	WriteLine(fileStream,"mob="+FindKeyCode(GUI_Message(menuControlsMobLabel, "gettext")))
	WriteLine(fileStream,"power_up="+FindKeyCode(GUI_Message(menuControlsPowerUpLabel, "gettext")))
	WriteLine(fileStream,"moveUp="+FindKeyCode(GUI_Message(menuTabEditorControlsForwardLabel, "gettext")))
	WriteLine(fileStream,"moveDown="+FindKeyCode(GUI_Message(menuTabEditorControlsBackwardLabel, "gettext")))
	WriteLine(fileStream,"moveLeft="+FindKeyCode(GUI_Message(menuTabEditorControlsLeftLabel, "gettext")))
	WriteLine(fileStream,"moveRight="+FindKeyCode(GUI_Message(menuTabEditorControlsRightLabel, "gettext")))
	WriteLine(fileStream,"moveFast="+FindKeyCode(GUI_Message(menuTabEditorControlsFastLabel, "gettext")))
	WriteLine(fileStream,"placeMany="+FindKeyCode(GUI_Message(menuTabEditorControlsPlaceManyLabel, "gettext")))
	CloseFile(fileStream)
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D