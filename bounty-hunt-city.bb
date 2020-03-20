; BOUNTY HUNT CITY
; (c) 2014 by Spark Fountain

;GUI and Particle System
Include "gui/DevilGUI.bb"
Include "particles/System/DevilParticleSystem.bb"

Dim testsound(3)

;Loaders
Include "modules/loaders.bb"

;Math
Include "modules/math.bb"

;Main Menu
Include "modules/main-menu.bb"

;Network
Include "modules/network.bb"

;Editor Modules
Include "modules/map-editor.bb"
Include "modules/mission-editor.bb"
Include "modules/vehicle-editor.bb"


AppTitle("Bounty Hunt City")
HidePointer()

SeedRnd(MilliSecs())

Global fileStream

;LOAD GAME SETTINGS
Global frameTimer
Global vsync
Global cam
Global light

;FONTS
Global hudFont

;game variables
Global msx, msy, msz, mszOld, mh2, mszSpeed
Global section$ = "init menu" ;init editor / menu / intro
Global map$		;name of current map
Global localPlayer.Human
Global localPlayerNumber
Global localPlayerAnimation$ = "none", singlePlayerAnimFrame# = 0
Global localPlayerWeapon, localPlayerMaxWeaponId
Global localPlayerCash = 0
Global rotateItemTimer

Global hudLife, hudWeapon, hudAmmo, hudCash, hudBountyHuntLevel

Global dontMove=False

Global gameClock#=6, gameClockTimer

Const MAX_DISTANCE_TO_NEXT_VEHICLE = 10
Const VEHICLE_COLLISION_DISTANCE = 10
Const HUMAN_BUILDING_COLLISION_DISTANCE = 50
Const HUMAN_VEHICLE_COLLISION_DISTANCE = 5
Const VEHICLE_BUILDING_COLLISION_DISTANCE = 70
Const VEHICLE_OVERRUN_SPEED = 15
Const FlatEntityY# = 0.006
Const VEHICLE_Y# = 0.11
Const HUMAN_Y# = 0.12
Const MOTOR_PITCH_MULTIPLICATOR = 2000
Const DISTANCE_FACTOR# = 1.6
Const LANGUAGE_PATH$ = "language/"
Const mainEditorWindowWidth = 320
Const HEADLIGHT_BREAK_SPEED = 20

;editor variables
Global mapSettingsGroup, isMultiPlayerRadio, isSinglePlayerRadio
Global loadSaveGroup, loadMapButton, saveMapButton, mapNameEdit

Global buildingId, vehicleId, humanId, itemId, triggerId

Global hoveringName$
Global hoveringEntity
Global hoveringCategory$

Global editObject	;handle
Global editOkButton, editUpdateButton
Global editBuildingWindow
Global editVehicleWindow, editVehicleEnergySlider, editVehicleEnergyLabel, editVehicleMaxEnergySlider, editVehicleMaxEnergyLabel
Global editHumanWindow, editHumanEnergySlider, editHumanEnergyLabel, editHumanMaxEnergySlider, editHumanMaxEnergyLabel, editHumanBehaviourComboBox
Global editItemWindow, editItemValueSlider, editItemValueLabel
Global editTriggerWindow, editTriggerBehaviourComboBox, editTriggerVariablesGroupBox

;WRECKS
Global carWreck

;SOUNDS
Global listener
Global carMotorSfx
Global vehicleExplodeSfx
Global bulletVehicleSfx
Global glassBreakSfx
Global overrunSfx
Const numOfHorns = 2
Dim horn(numOfHorns)

;SPECIAL SOUNDS
Global fartChannel
Dim fart(3)

;SOUND CHANNELS
Global channelAmbient
Global channelSpeech
Global channelEffects
Global channelMusic

;TEMPLATES
Global vehicleBurn1
Global vehicleBurn2
Global vehicleBurn3
Global vehicleExplode

;GUI
Global guiFirstInitDone = 0
Global languageSelected$

;FPS INDEPENDENT MOVEMENTS
Global fps, fpsInc, fpsTimer

InitGame()


;NETWORK STUFF
Global udpInputStream, udpOutputStream
Const MAX_NUM_OF_CLIENTS = 6
Const UDP_INPUT_PORT = 4000
Const UDP_OUTPUT_PORT = 4001
CountHostIPs(GetEnv("localhost"))
Global LOCALHOST_IP_ADDRESS$ = DottedIP(HostIP(1))
Const UDP_TIMEOUT = 5000
Global udpTimeCounter


;MAIN LOOP
MainLoop()

Function MainLoop()
	Repeat
		If(fpsTimer<MilliSecs()) Then
			fpsTimer=MilliSecs()+1000
			fps=fpsInc
			fpsInc=0
		EndIf
		
		msx = MouseX()
		msy = MouseY()
		mszOld = msz
		msz = MouseZ()
		mh2 = MouseHit(2)
		mszSpeed = MouseZSpeed()
		
		Select section
			Case "init intro"
				Local logo = CreateCube()
				Local logoTexture = LoadTexture("gfx/logo.png")
				EntityTexture(logo,logoTexture)
				PositionEntity(logo,0,0,20)
				Local switchToMenuTime = MilliSecs()+7000
				Local fade = CreateCube() : EntityColor(fade,0,0,0) : PositionEntity(fade,0,0,2) : EntityAlpha(fade,0.0)
				Local logosound = LoadSound("sfx/logo.ogg")
				Local introchannel = PlaySound(logosound)
				section = "intro"
			Case "intro"
				Cls()
				WaitTimer(frameTimer)
				
				If(EntityZ(logo) > 2.5) Then
					TurnEntity(logo,0,0,8)
					MoveEntity(logo,0,0,-0.2)
					If(EntityZ(logo) < 2.5) Then PositionEntity(logo,EntityX(logo),EntityY(logo),2.5)
				Else
					RotateEntity(logo,0,0,355-(Float((switchToMenuTime-MilliSecs()))/900))
				EndIf
				
				;fade out
				If((switchToMenuTime-1000) < MilliSecs()) Then
					EntityAlpha(fade,(1-(Float((switchToMenuTime-MilliSecs()))/1000)))
				EndIf
				
				;switch to menu
				If((switchToMenuTime < MilliSecs()) Or KeyHit(1) Or KeyHit(57) Or KeyHit(28) Or MouseHit(1)) Then
					FreeEntity(logo)
					FreeTexture(logoTexture)
					FreeEntity(fade)
					StopChannel(introchannel)
					section = "init menu"
				EndIf
				
				UpdateWorld()
				RenderWorld()
				
				Flip(vsync)
			Case "init menu"
				InitMenu2()
				section = "menu"
			Case "menu"
				Cls()
				WaitTimer(frameTimer)
				
				MenuEvents()
				Quit()
				;...
				
				GUI_UpdateGUI()
				Flip(vsync)
			Case "init editor"
				;for now, close menu window
				GUI_Message(menuWindow, "close")
				
				;create gui
				mainEditorWindow = GUI_CreateWindow(0, -27, mainEditorWindowWidth, GetInt("graphics","screenHeight")+27, "Main Window", "", False, False, False)
				GUI_Message(mainEditorWindow, "setlocked", True)
				
				mapSettingsGroup = GUI_CreateGroupBox(mainEditorWindow, 10, 10, 300, 90, "Map Settings")	;replace later by locale string
				isSinglePlayerRadio = GUI_CreateRadio(mapSettingsGroup, 20, 30, "Singleplayer", 0, True, True)
				isMultiPlayerRadio = GUI_CreateRadio(mapSettingsGroup, 20, 50, "Multiplayer")
				
				loadSaveGroup = GUI_CreateGroupBox(mainEditorWindow, 10, 110, 300, 90, "Load & Save")	;replace later by locale string
				mapNameEdit = GUI_CreateEdit(loadSaveGroup, 20, 32, 150)
				loadMapButton = GUI_CreateButton(loadSaveGroup, 190, 15, 75, 25, "Load Map")
				saveMapButton = GUI_CreateButton(loadSaveGroup, 190, 45, 75, 25, "Save Map")
				
				tabMain = GUI_CreateTab(mainEditorWindow, 10, 210, mainEditorWindowWidth-20, GetInt("graphics","screenHeight")-225)
				tabBuildings = GUI_CreateTabPage(tabMain, "Buildings") : CreateThumbnails("buildings",tabBuildings)
				tabVehicles = GUI_CreateTabPage(tabMain, "Vehicles") : CreateThumbnails("vehicles",tabVehicles)
				tabHumans = GUI_CreateTabPage(tabMain, "Humans") : CreateThumbnails("humans",tabHumans)
				tabItems = GUI_CreateTabPage(tabMain, "Items") : CreateThumbnails("items",tabItems)
				tabTriggers = GUI_CreateTabPage(tabMain, "Triggers") : CreateThumbnails("triggers",tabTriggers)
				GUI_Message(tabMain, "SetIndex", 1)
				
				;some environment stuff
				Local plane = CreatePlane()
				EntityTexture(plane,LoadTexture("gfx/water.png"))
				EntityPickMode(plane,2)
				
				;adjust camera
				PositionEntity(cam,0,25,0)
				RotateEntity(cam,90,0,0)
				
				section = "editor"
			Case "editor"
				Cls()
				WaitTimer(frameTimer)
				
				BackToMenu(True)
				
				UpdateWorld()
				RenderWorld()
				
				ScrollerEvents()
				ThumbnailEvents()
				ButtonEvents()
				PositionHovering()
				Pick()
				MoveOnMap()
				OpenEditWindow()
				UpdateEditWindows()
				GUI_UpdateGUI()
				
				;Text(5,5,"Last Event: "+GUI_AppEvent())
				
				Flip(vsync)
			Case "init single player"
				LoadGameStuff()
				
				;start game
				section = "single player game"
			Case "init local network"
				LoadGameStuff()
				
				;start game
				section = "local network game"
			Case "single player game"
				Cls()
				WaitTimer(frameTimer)
				
				InGameFunctionsBeforeRendering()
				
				UpdateWorld()
				UpdateParticles()
				RenderWorld()
				
				InGameFunctionsAfterRendering()
				
				Flip(1)
				
			Case "local network game"
				Cls()
				WaitTimer(frameTimer)
				
				InGameFunctionsBeforeRendering()
				
				UpdateWorld()
				UpdateParticles()
				RenderWorld()
				
				InGameFunctionsAfterRendering()
				
				BackToMenu()
				
				Flip(1)
		End Select
		
		fpsInc = fpsInc + 1
	Forever
End Function

Function LoadGameStuff()
	InitGame()
	
	;some environment stuff
	plane = CreatePlane()
	EntityTexture(plane,LoadTexture("gfx/plane.png"))
	
	;init camera
	PositionEntity(cam,0,20,0)
	RotateEntity(cam,90,0,0)
	
	;load map
	LoadMap(map,False)
	If(Handle(localPlayer)=0) Then RuntimeError("No local network player on that map")
	
	;load weapons
	LoadWeapons()
	
	;load hud
	LoadHud()
	
	;place some cones
;	For x=-50 To 50
;		For z=-50 To 50
;			Local cone = CreateCone()
;			EntityColor(cone,255,255,0)
;			PositionEntity(cone,x*10,0,z*10)
;		Next
;	Next
End Function

Function InGameFunctionsBeforeRendering()
	MoveLocalPlayer()
	MoveVehicles()
	VehicleEmitters()
	VehicleActions()
	UpdateLights()
	AnimateLocalPlayer()
	SetEnergy()
	RotateItems()
	Collect()
	If(Not(localPlayer\inVehicle)) Then Attack()
	MoveBullets()
	SwitchWeapon()
	;DayTime()
	Collide()
End Function

Function InGameFunctionsAfterRendering()
	HUD()
	
	;debug info
	Text(0, GetInt("graphics","screenHeight")-20, "player x,y,z: "+EntityX(localPlayer\entity)+","+EntityY(localPlayer\entity)+","+EntityZ(localPlayer\entity))
;	Text(0,40,"Game clock: "+Int(gameClock)+" h")
;	Text(0,GetInt("graphics","screenHeight")-40,"fps: "+fps)
	
	If(localPlayer\inVehicle) Then
		For v.Vehicle = Each Vehicle
			If(v\entity = localPlayer\inVehicle) Then
				Text(GetInt("graphics","screenWidth")/2,20,"Speed: "+Int(v\speed)+" km/h",GetInt("graphics","screenWidth")/2)
				Exit
			EndIf
		Next
	EndIf
	
	;hud info
	;Text(GetInt("graphics","screenWidth")-100,0,"Energy: "+singlePlayer\energy)
	
	BackToMenu()
End Function

Function InitGame()
	If(guiFirstInitDone) Then 
		GUI_FreeGUI()
		FreeParticles()
	EndIf
	
	EndGraphics()
	DeleteVars()
	DeleteLoadOnlys()
	InitAll("game.ini")
	languageSelected = GetString("language","dir")
	Graphics3D(GetInt("graphics","screenWidth"), GetInt("graphics","screenHeight"), 32, GetInt("graphics","screenMode"))
	SetBuffer(BackBuffer())
	frameTimer = CreateTimer(60)
	vsync = GetInt("graphics","vsync")
	cam = CreateCamera()
	light = CreateLight()
	
	;LOAD FONTS
	hudFont = LoadFont("Comic Sans MS",20,1)
	
	;LOAD KEY DEFINITIONS
	InitAll("keys.ini")
	
	;LOAD BUILDINGS
	LoadBuildings()
	
	;LOAD VEHICLES
	LoadVehicles()
	
	;LOAD HUMANS
	LoadHumans()
	
	;LOAD BEHAVIOURS
	LoadBehaviours()
	
	;LOAD ITEMS
	LoadItems()
	
	;LOAD TRIGGERS
	LoadTriggers()
	
	;LOAD VEHICLE CONFIG
	InitAll("vehicles.ini")
	
	;LOAD LIGHTS CONFIG
	InitAll("lights.ini")
	
	;LOAD WEAPONS
	InitAll("weapons.ini")
	
	;LOAD SOUNDS
	listener = CreatePivot()
	CreateListener(listener,0.3,1,10)
	carMotorSfx = Load3DSound("sfx/car_motor.ogg")
	vehicleExplodeSfx = Load3DSound("sfx/vehicle_explode.ogg")
	glassBreakSfx = Load3DSound("sfx/glassBreak.ogg")
	overrunSfx = Load3DSound("sfx/overrun.ogg")
	For i=0 To numOfHorns
		horn(i) = Load3DSound("sfx/horn"+i+".ogg")
	Next
	
	For i=0 To 3
		testsound(i) = LoadSound("sfx/test"+i+".ogg")
	Next
	
	;WRECKS
	carWreck = LoadTexture("gfx/vehicles/wrecks/car_wreck.png",4)
	
	;LOAD WEAPON SOUNDS
	bulletVehicleSfx = Load3DSound("sfx/bullet_vehicle.ogg")
	SoundVolume(bulletVehicleSfx,1)
	
	;LOAD SPECIAL SOUNDS
	For f=0 To 3
		fart(f) = LoadSound("sfx/fart"+f+".ogg")
	Next
	
	;PARTICLE ENGINE
	InitParticles(cam)
	
	;TEMPLATES
	vehicleBurn1 = CreateTemplate()
	SetTemplateEmitterBlend(vehicleBurn1, 1)
	SetTemplateInterval(vehicleBurn1, 1)
	SetTemplateEmitterLifeTime(vehicleBurn1, -1)
	SetTemplateParticleLifeTime(vehicleBurn1, 15, 20)
	SetTemplateTexture(vehicleBurn1, "gfx/vehicle_burn.png", 4, 1)
	SetTemplateOffset(vehicleBurn1, -0.1, 0.1, -0.1, 0.1, -0.1, 0.1)
	SetTemplateVelocity(vehicleBurn1, -0.04, 0.04, 0.1, 0.2, -0.04, 0.04)
	SetTemplateAlphaVel(vehicleBurn1, True)
	SetTemplateSize(vehicleBurn1, 0.2, 0.2, 0.3, 0.6)
	SetTemplateSizeVel(vehicleBurn1, 0.02, 1)
	
	vehicleBurn2 = CreateTemplate()
	SetTemplateEmitterBlend(vehicleBurn2, 1)
	SetTemplateInterval(vehicleBurn2, 1)
	SetTemplateEmitterLifeTime(vehicleBurn2, -1)
	SetTemplateParticleLifeTime(vehicleBurn2, 25, 35)
	SetTemplateTexture(vehicleBurn2, "gfx/vehicle_burn.png", 4, 1)
	SetTemplateOffset(vehicleBurn2, -.3, .3, -.3, .3, -.3, .3)
	SetTemplateVelocity(vehicleBurn2, -.04, .04, .1, .2, -.04, .04)
	SetTemplateAlphaVel(vehicleBurn2, True)
	SetTemplateSize(vehicleBurn2, 0.6, 0.6, 0.3, 0.8)
	SetTemplateSizeVel(vehicleBurn2, .01, 1.01)
	
	vehicleBurn3 = CreateTemplate()
	SetTemplateEmitterBlend(vehicleBurn3, 1)
	SetTemplateInterval(vehicleBurn3, 1)
	SetTemplateEmitterLifeTime(vehicleBurn3, -1)
	SetTemplateParticleLifeTime(vehicleBurn3, 40, 55)
	SetTemplateTexture(vehicleBurn3, "gfx/vehicle_burn.png", 4, 1)
	SetTemplateOffset(vehicleBurn3, -.3, .3, -.3, .3, -.3, .3)
	SetTemplateVelocity(vehicleBurn3, -.04, .04, .1, .2, -.04, .04)
	SetTemplateAlphaVel(vehicleBurn3, True)
	SetTemplateSize(vehicleBurn3, 1, 1, 0.3, 0.8)
	SetTemplateSizeVel(vehicleBurn3, .01, 1.01)
	
	vehicleExplode = CreateTemplate()
	SetTemplateEmitterBlend(vehicleExplode, 1)
	SetTemplateInterval(vehicleExplode, 1)
	SetTemplateEmitterLifeTime(vehicleExplode, 30)
	SetTemplateParticleLifeTime(vehicleExplode, 40, 55)
	SetTemplateTexture(vehicleExplode, "gfx/vehicle_explode.png", 4, 1)
	SetTemplateOffset(vehicleExplode, -.3, .3, -.3, .3, -.3, .3)
	SetTemplateVelocity(vehicleExplode, -.04, .04, .1, .2, -.04, .04)
	SetTemplateAlphaVel(vehicleExplode, True)
	SetTemplateSize(vehicleExplode, 2, 2, 0.4, 2.5)
	SetTemplateSizeVel(vehicleExplode, .01, 1.01)
	
	;LOAD LANGUAGE FILES
	InitAll(LANGUAGE_PATH+GetString("language","dir")+"/menu.ini")
	InitAll(LANGUAGE_PATH+GetString("language","dir")+"/keynames.ini","keyname")
	
	;INIT GUI
	GUI_InitGUI("gui\Skins\backrow.skin")
	guiFirstInitDone=1
End Function

Function FindKeyCode(keyName$)
	Local keyNameStream = ReadFile(LANGUAGE_PATH+GetString("language","dir")+"/keynames.ini")
	Local i=1
	While(Not(Eof(keyNameStream)))
		Local currentLine$ = ReadLine(keyNameStream)
		If(currentLine = keyName) Then
			Return i
		EndIf
		i=i+1
	Wend
	CloseFile(keyNameStream)
End Function

Function GetUserKey$()
	FlushKeys()
	Local msgBox = GUI_CreateWindow(-1,-1,200,100,"wait for key","","",False,False,False)
	GUI_Message(msgBox,"setlocked",True)
	DebugLog("check")
	GUI_CreateLabel(msgBox,100,25,GetString("tabControls","pressKey"),"center")
	Repeat
		For i=1 To 237
			If(KeyHit(i)) Then
				GUI_Message(msgBox, "close")
				GUI_Message(menuWindow,"bringtofront")
				Return GetString("keyname",Str(i))
			EndIf
		Next
		GUI_UpdateGui()
		Flip(vsync)
	Forever
End Function

Function CreateThumbnails(category$, tab)
	Local dir = ReadDir("gfx/"+category)
	Local index = 0
	Repeat 
		Local currentFile$ = NextFile$(dir) 
		If currentFile = "" Then Exit 
		If((Right(currentFile,3) = "png") And (Not(Instr(currentFile,"_tex")))) Then
			th.Thumbnail = New Thumbnail
			th\name = Left(currentFile,Len(currentFile)-4)
			th\category = category
			th\id = GUI_CreateButton(tab, 14+((index Mod 3)*(64+14)), 14+((index/3)*(64+14)), 64, 64, "")
			GUI_Message(th\id, "seticon", "gfx/"+category+"/"+currentFile, 64, 64)
			th\y = 14+((index/3)*(64+14))
			index=index+1
			;DebugLog("created "+th\category+" named "+th\name)
		EndIf
	Forever 
	CloseDir(dir)
	
	;scroll bar
	If(index>23) Then
		Local s.Scroller = New Scroller
		s\name = category
		s\id = GUI_CreateScrollBar(tab, 250, 14, GetInt("graphics","screenHeight")-160)
		s\length = (index/3)*(64+14) - (GetInt("graphics","screenHeight") - 300)
		
		HideInvisibleThumbnails(s\id)
	EndIf
End Function

Function ScrollerEvents()
	For s.Scroller = Each Scroller
		If(GUI_AppEvent() = s\id) Then
			HideInvisibleThumbnails(s\id)
			Exit
		EndIf
	Next
End Function

Function HideInvisibleThumbnails(id)
	For s.Scroller = Each Scroller
		If(s\id = id) Then
			For t.Thumbnail = Each Thumbnail
				Local newY = t\y - (s\length / 100) * Int(GUI_Message(s\id,"getstatus"))
				GUI_Message(t\id,"setpos",GUI_Message(t\id,"getx"),newY)
				If(newY<0 Or newY>GetInt("graphics","screenHeight")-205) Then
					GUI_Message(t\id,"hide")
				Else
					GUI_Message(t\id,"show")
				EndIf
			Next
		EndIf
	Next
End Function

Function ThumbnailEvents()
	For th.Thumbnail = Each Thumbnail
		If(GUI_AppEvent() = th\id) Then
			hoveringName = th\name
			hoveringCategory = th\category
			Select th\category
				Case "buildings"	
					;create a new entity of this building
					For b.Building = Each Building
						If(b\name = th\name) Then
							Local newB.Building = New Building
							newB\id = buildingId : buildingId=buildingId+1
							newB\entity = CopyEntity(b\entity)
							newB\name = hoveringName
							hoveringEntity = newB\entity
							newB\loadonly = False
							Exit
						EndIf
					Next
				Case "vehicles"
					;create a new entity of this vehicle
					For v.Vehicle = Each Vehicle
						If(v\name = th\name And v\loadonly) Then
							Local newV.Vehicle = New Vehicle
							newV\id = vehicleId : vehicleId=vehicleId+1
							newV\name = hoveringName
							newV\entity = CreateCube() : ScaleEntity(newV\entity,GetFloat(v\name,"scale_x"),FlatEntityY,GetFloat(v\name,"scale_z"))
							MoveEntity(newV\entity,0,VEHICLE_Y,0)
							EntityTexture(newV\entity, v\entity)
							hoveringEntity = newV\entity
							newV\loadonly = False
							Exit
						EndIf
					Next
				Case "humans"
					;create a new entity of this human
					For h.Human = Each Human
						If(h\name = th\name) Then
							Local newH.Human = New Human
							newH\id = humanId : humanId=humanId+1
							newH\entity = CreateCube() : ScaleEntity(newH\entity,1,FlatEntityY,1)
							MoveEntity(newH\entity,0,HUMAN_Y,0)
							EntityTexture(newH\entity, h\entity)
							hoveringEntity = newH\entity
							newH\name = hoveringName
							newH\loadonly = False
							Exit
						EndIf
					Next	
				Case "items"
					;create a new entity of this item
					For i.Item = Each Item
						If(i\name = th\name) Then
							Local newI.Item = New Item
							newI\id = itemId : itemId=itemId+1
							newI\entity = CreateCube() : ScaleEntity(newI\entity,1,FlatEntityY,1)
							MoveEntity(newI\entity,0,0.12,0)
							EntityTexture(newI\entity, i\entity)
							EntityFX(newI\entity,1)
							hoveringEntity = newI\entity
							newI\name = hoveringName
							newI\loadonly = False
							Exit
						EndIf
					Next
				Case "triggers"
					;create a new entity of this trigger
					For t.Trigger = Each Trigger
						If(t\name = th\name) Then
							Local newT.Trigger = New Trigger
							newT\id = triggerId : triggerId=triggerId+1
							newT\entity = CreateCube() : ScaleEntity(newT\entity,1,FlatEntityY,1)
							MoveEntity(newT\entity,0,0.12,0)
							EntityTexture(newT\entity, t\entity)
							EntityFX(newT\entity,1)
							hoveringEntity = newT\entity
							newT\name = hoveringName
							newT\loadonly = False
							Exit
						EndIf
					Next
			End Select
			Exit
		EndIf
	Next
End Function

Function ButtonEvents()
	Local fileName$
	If(GUI_AppEvent() = loadMapButton) Then
		fileName = GUI_Message(mapNameEdit,"gettext")
		If(fileName = "") Then
			GUI_MsgBox("Error: No map name","Please type the map name you want to load.")
		Else
			If(GUI_Message(isSinglePlayerRadio,"getchecked") = True) Then
				LoadMap(fileName)
			ElseIf(GUI_Message(isMultiPlayerRadio,"getchecked") = True) Then
				LoadMap("multiplayer/"+fileName)
			EndIf
		EndIf
	ElseIf(GUI_AppEvent() = saveMapButton) Then
		fileName = GUI_Message(mapNameEdit,"gettext")
		If(fileName = "") Then
			GUI_MsgBox("Error: No map name","Please type the map name you want to save.")
		Else
			If(GUI_Message(isSinglePlayerRadio,"getchecked") = True) Then
				SaveMap(fileName)
			ElseIf(GUI_Message(isMultiPlayerRadio,"getchecked") = True) Then
				SaveMap("multiplayer/"+fileName)
			EndIf
		EndIf
	EndIf
End Function

Function OpenEditWindow()
	If(mh2) Then
		Local obj = CameraPick(cam,msx,msy)
		Local groupbox, groupbox2, groupbox3
		
		;building?
		For b.Building = Each Building
			If(obj = b\entity) Then
				
			EndIf
		Next
		
		;vehicle?
		For v.Vehicle = Each Vehicle
			If(obj = v\entity) Then
				editObject = Handle(v)
				editVehicleWindow = GUI_CreateWindow(GetInt("graphics","screenWidth")/2+150-100, GetInt("graphics","screenHeight")/2-335/2, 200, 335, v\name+" (id="+v\id+")", "", False, False, False)
				groupbox = GUI_CreateGroupBox(editVehicleWindow, 10, 13, 180, 60, "Energy")
				editVehicleEnergySlider = GUI_CreateSlider(groupbox, 10, 20, 120, v\energy, 1, 100)
				editVehicleEnergyLabel = GUI_CreateLabel(groupbox, 140, 22, Int(GUI_Message(editVehicleEnergySlider, "getvalue")))
				groupbox2 = GUI_CreateGroupBox(editVehicleWindow, 10, 13+70, 180, 60, "Maximum Energy")
				editVehicleMaxEnergySlider = GUI_CreateSlider(groupbox2, 10, 20, 120, v\maxEnergy, 1, 100)
				editVehicleMaxEnergyLabel = GUI_CreateLabel(groupbox2, 140, 22, Int(GUI_Message(editVehicleMaxEnergySlider, "getvalue")))
				editOkButton = GUI_CreateButton(editVehicleWindow, 80, 270, 50, 20, "OK")
			EndIf
		Next
		
		;human?
		For h.Human = Each Human
			If(obj = h\entity) Then
				editObject = Handle(h)
				editHumanWindow = GUI_CreateWindow(GetInt("graphics","screenWidth")/2+150-100, GetInt("graphics","screenHeight")/2-335/2, 200, 335, h\name+" (id="+h\id+")", "", False, False, False)
				groupbox = GUI_CreateGroupBox(editHumanWindow, 10, 13, 180, 60, "Energy")
				editHumanEnergySlider = GUI_CreateSlider(groupbox, 10, 20, 120, h\energy, 1, 100)
				editHumanEnergyLabel = GUI_CreateLabel(groupbox, 140, 22, Int(GUI_Message(editHumanEnergySlider, "getvalue")))
				groupbox2 = GUI_CreateGroupBox(editHumanWindow, 10, 13+70, 180, 60, "Maximum Energy")
				editHumanMaxEnergySlider = GUI_CreateSlider(groupbox2, 10, 20, 120, h\maxEnergy, 1, 100)
				editHumanMaxEnergyLabel = GUI_CreateLabel(groupbox2, 140, 22, Int(GUI_Message(editHumanMaxEnergySlider, "getvalue")))
				groupbox3 = GUI_CreateGroupBox(editHumanWindow, 10, 13+70+70, 180, 110, "Behaviour")
				editHumanBehaviourComboBox = GUI_CreateComboBox(groupbox3, 10, 20, 150)
				Local iterator, behIndex
				For beh.Behaviour = Each Behaviour
					If(beh\category = "humans") Then
						GUI_Message(editHumanBehaviourComboBox, "additem", -1, beh\name)
						If(beh\name = h\behaviour) Then behIndex = iterator
						iterator=iterator+1
					EndIf
				Next
				GUI_Message(editHumanBehaviourComboBox, "setselected", behIndex)
				editOkButton = GUI_CreateButton(editHumanWindow, 80, 270, 50, 20, "OK")
			EndIf
		Next
		
		;item?
		For i.Item = Each Item
			If(obj = i\entity) Then
				editObject = Handle(i)
				editItemWindow = GUI_CreateWindow(GetInt("graphics","screenWidth")/2+150-100, GetInt("graphics","screenHeight")/2-135/2, 200, 135, i\name+" (id="+i\id+")", "", False, False, False)
				groupbox = GUI_CreateGroupBox(editItemWindow, 10, 13, 180, 80, "Set Value")
				editItemValueSlider = GUI_CreateSlider(groupbox, 10, 20, 120, i\value, 1, 100)
				editItemValueLabel = GUI_CreateLabel(groupbox, 140, 20, Int(GUI_Message(editItemValueSlider, "getvalue")))
				editOkButton = GUI_CreateButton(groupbox, 10, 50, 50, 20, "OK")
			EndIf
		Next
		
		;trigger?
		For t.Trigger = Each Trigger
			If(obj = t\entity) Then
				editObject = Handle(t)
				editTriggerWindow = GUI_CreateWindow(GetInt("graphics","screenWidth")/2+150-100, GetInt("graphics","screenHeight")/2-135/2, 200, 230, t\name+" (id="+t\id+")", "", False, False, False)
				groupbox = GUI_CreateGroupBox(editTriggerWindow, 10, 13, 180, 90, "Behaviour")
				editTriggerBehaviourComboBox = GUI_CreateComboBox(groupbox, 10, 20, 150)
				For beh.Behaviour = Each Behaviour
					If(beh\category = "triggers") Then
						GUI_Message(editTriggerBehaviourComboBox, "additem", -1, beh\name)
						If(beh\name = t\behaviour) Then behIndex = iterator
						iterator=iterator+1
					EndIf
				Next
				GUI_Message(editTriggerBehaviourComboBox, "setselected", behIndex)
				editTriggerVariablesGroupBox = GUI_CreateGroupBox(editTriggerWindow, 10, 113, 180, 30, "Variables")
				editUpdateButton = GUI_CreateButton(groupbox, 90-25, 50, 50, 20, "Update")
				editOkButton = GUI_CreateButton(editTriggerWindow, 100-25, 160, 50, 20, "Ok")
				UpdateEditWindow("triggers", t\id)
			EndIf
		Next
	EndIf
End Function

Function UpdateEditWindows()
	If(editVehicleWindow) Then
		GUI_Message(editVehicleEnergyLabel, "settext", Int(GUI_Message(editVehicleEnergySlider, "getvalue")))
		GUI_Message(editVehicleMaxEnergyLabel, "settext", Int(GUI_Message(editVehicleMaxEnergySlider, "getvalue")))
		If(GUI_AppEvent() = editOkButton) Then
			;energy must not be greater than maxEnergy
			If(Int(GUI_Message(editVehicleEnergySlider, "getvalue")) > Int(GUI_Message(editVehicleMaxEnergySlider, "getvalue"))) Then
				GUI_MsgBox("Error","The energy must not be greater than maximum energy.")
				Return
			EndIf
			
			For v.Vehicle = Each Vehicle
				If(Handle(v) = editObject) Then
					v\energy = Int(GUI_Message(editVehicleEnergySlider, "getvalue"))
					v\maxEnergy = Int(GUI_Message(editVehicleMaxEnergySlider, "getvalue"))
					GUI_Message(editVehicleWindow, "close") : editVehicleWindow = 0
					gui\mh1 = 0
					Return
				EndIf
			Next
		EndIf
	ElseIf(editHumanWindow) Then
		GUI_Message(editHumanEnergyLabel, "settext", Int(GUI_Message(editHumanEnergySlider, "getvalue")))
		GUI_Message(editHumanMaxEnergyLabel, "settext", Int(GUI_Message(editHumanMaxEnergySlider, "getvalue")))
		If(GUI_AppEvent() = editOkButton) Then
			;energy must not be greater than maxEnergy
			If(Int(GUI_Message(editHumanEnergySlider, "getvalue")) > Int(GUI_Message(editHumanMaxEnergySlider, "getvalue"))) Then
				GUI_MsgBox("Error","The energy must not be greater than maximum energy.")
				Return
			EndIf
			
			For h.Human = Each Human
				If(Handle(h) = editObject) Then
					h\energy = Int(GUI_Message(editHumanEnergySlider, "getvalue"))
					h\maxEnergy = Int(GUI_Message(editHumanMaxEnergySlider, "getvalue"))
					h\behaviour = GUI_Message(editHumanBehaviourComboBox, "gettext")
					GUI_Message(editHumanWindow, "close") : editHumanWindow = 0
					gui\mh1 = 0
					Return
				EndIf
			Next
		EndIf
	ElseIf(editItemWindow) Then
		GUI_Message(editItemValueLabel, "settext", Int(GUI_Message(editItemValueSlider, "getvalue")))
		If(GUI_AppEvent() = editOkButton) Then
			For i.Item = Each Item
				If(Handle(i) = editObject) Then
					i\value = Int(GUI_Message(editItemValueSlider, "getvalue"))
					GUI_Message(editItemWindow, "close") : editItemWindow = 0
					gui\mh1 = 0
					Return
				EndIf
			Next
		EndIf
	ElseIf(editTriggerWindow) Then
		If(GUI_AppEvent() = editUpdateButton) Then
			For t.Trigger = Each Trigger
				If(Handle(t) = editObject) Then
					DeleteBehVariables("triggers", t\id)
					t\behaviour = GUI_Message(editTriggerBehaviourComboBox, "gettext")
					ParseScript(t\behaviour, "triggers", t\id)
					UpdateEditWindow("triggers", t\id)
					Return
				EndIf
			Next
		ElseIf(GUI_AppEvent() = editOkButton) Then
			For t.Trigger = Each Trigger
				If(Handle(t) = editObject) Then
					For guiVar.BehGuiVar = Each BehGuiVar
						If((guiVar\category = "triggers") And (guiVar\objId = t\id)) Then
							;integer?
							For behInt.BehInteger = Each BehInteger
								If((behInt\category = "triggers") And (behInt\objId = t\id) And (behInt\name = GUI_Message(guiVar\label, "gettext"))) Then
									behInt\value = GUI_Message(guiVar\edit, "gettext")
								EndIf
							Next
							
							;string?
							For behString.BehString = Each BehString
								If((behString\category = "triggers") And (behString\objId = t\id) And (behString\name = GUI_Message(guiVar\label, "gettext"))) Then
									behString\value = GUI_Message(guiVar\edit, "gettext")
								EndIf
							Next
							
							;float?
							For behFloat.BehFloat = Each BehFloat
								If((behFloat\category = "triggers") And (behFloat\objId = t\id) And (behFloat\name = GUI_Message(guiVar\label, "gettext"))) Then
									behFloat\value = GUI_Message(guiVar\edit, "gettext")
								EndIf
							Next
						EndIf
					Next
					GUI_Message(editTriggerWindow, "close") : editTriggerWindow = 0
					gui\mh1 = 0
					Return
				EndIf
			Next
		EndIf
	EndIf
End Function

Function UpdateEditWindow(category$, elementId)
	Local offsetY = 20
	
	;show all object variables
	For behInt.BehInteger = Each BehInteger
		If(behInt\objId = elementId) Then
			Select category
				Case "triggers"
					Local guiVar.BehGuiVar = New BehGuiVar
					guiVar\objId = elementId
					guiVar\category = category
					guiVar\edit = GUI_CreateEdit(editTriggerVariablesGroupBox, 10, offsetY, 30, behInt\value)
					guiVar\label = GUI_CreateLabel(editTriggerVariablesGroupBox, 50, offsetY+3, behInt\name)
					offsetY=offsetY+30
					DebugLog("object has int '"+behInt\name+"'")
			End Select
		EndIf
	Next
	
	For behString.BehString = Each BehString
		If(behString\objId = elementId) Then
			Select category
				Case "triggers"
					guiVar.BehGuiVar = New BehGuiVar
					guiVar\objId = elementId
					guiVar\category = category
					guiVar\edit = GUI_CreateEdit(editTriggerVariablesGroupBox, 10, offsetY, 60, behString\value)
					guiVar\label = GUI_CreateLabel(editTriggerVariablesGroupBox, 80, offsetY+3, behString\name)
					offsetY=offsetY+30
					DebugLog("object has string '"+behString\name+"'")
			End Select
		EndIf
	Next
	
	For behFloat.BehFloat = Each BehFloat
		If(behFloat\objId = elementId) Then
			Select category
				Case "triggers"
					guiVar.BehGuiVar = New BehGuiVar
					guiVar\objId = elementId
					guiVar\category = category
					guiVar\edit = GUI_CreateEdit(editTriggerVariablesGroupBox, 10, offsetY, 30, behFloat\value)
					guiVar\label = GUI_CreateLabel(editTriggerVariablesGroupBox, 50, offsetY+3, behFloat\name)
					offsetY=offsetY+30
					DebugLog("object has float '"+behFloat\name+"'")
			End Select
		EndIf
	Next
	
	;resize window and elements
	GUI_Message(editTriggerVariablesGroupBox, "setsize", 180, offsetY)
	GUI_Message(editTriggerWindow, "setsize", 200, offsetY+190)
	GUI_Message(editOkButton, "setpos", 100-25, offsetY+120)
End Function

Function ParseScript(behName$, category$, objId)
	DebugLog("parse behaviour "+behName+" ("+category+")")
	Local lineNr = 1
	Repeat
		lineNr = ParseLine(lineNr,behName$, category$, objId)
		DebugLog("parse line "+lineNr)
		If(lineNr=0) Then Exit
	Forever
End Function

Function ParseLine(lineNr,behName$, category$, objId=-1)
	For sl.ScriptLine = Each ScriptLine
		If((sl\behaviour = behName) And (sl\category = category) And (sl\lineNr = lineNr)) Then
			;integer?
			If(Left(sl\txt,3) = "int") Then
				Local behInt.BehInteger = New BehInteger
				behInt\category = category
				behInt\objId = objId
				behInt\name = Mid(sl\txt,5)
				behInt\value = 0
				;DebugLog("found int "+behInt\name)
				Return lineNr+1
			EndIf
			
			;string?
			If(Left(sl\txt,6) = "string") Then
				Local behString.BehString = New BehString
				behString\category = category
				behString\objId = objId
				behString\name = Mid(sl\txt,8)
				behString\value = ""
				;DebugLog("found string "+behString\name)
				Return lineNr+1
			EndIf
			
			;float?
			If(Left(sl\txt,5) = "float") Then
				Local behFloat.BehFloat = New BehFloat
				behFloat\category = category
				behFloat\objId = objId
				behFloat\name = Mid(sl\txt,7)
				behFloat\value = 0.0
				;DebugLog("found float "+behFloat\name)
				Return lineNr+1
			EndIf
			
			;foreach?
			If(Left(sl\txt,7) = "foreach") Then
				DebugLog("parse foreach")
				ScriptForEach(lineNr+1, category, behName)
			EndIf
			
			;endforeach?
			If(Left(sl\txt,10) = "endforeach") Then
				DebugLog("endforeach")
				Return 0
			EndIf
			
			;if?
			If(Left(sl\txt,2) = "if") Then
				ScriptIf()
			EndIf
			
			;endif?
			If(Left(sl\txt,5) = "endif") Then
				DebugLog("endif")
				Return 0
			EndIf
			
			;debug
			DebugLog("nothing found")
			lineNr=lineNr+1
			
		EndIf
	Next
End Function

Function ScriptForEach(lineNrParam, category$, behName$)
	Local lineNr = lineNrParam
	Select category
		Case "triggers"
			For t.Trigger = Each Trigger
				If(Not(t\loadonly)) Then
					If(t\behaviour = behName) Then
						DebugLog("check")
						;continue parsing
						Repeat
							DebugLog("foreach: parse line "+lineNr)
							lineNr = ParseLine(lineNr,behName,category)
							If(lineNr=0) Then Exit
						Forever
					EndIf
				EndIf
			Next
	End Select
End Function

Function ScriptIf()
	;Local lineNr = 
End Function

Function DeleteBehVariables(category$, elementId)
	For behInt.BehInteger = Each BehInteger
		If((behInt\category = category) And (behInt\objId = elementId)) Then
			Delete behInt
		EndIf
	Next
	
	For behString.BehString = Each BehString
		If((behString\category = category) And (behString\objId = elementId)) Then
			Delete behString
		EndIf
	Next
	
	For behFloat.BehFloat = Each BehFloat
		If((behFloat\category = category) And (behFloat\objId = elementId)) Then
			Delete behFloat
		EndIf
	Next
End Function

Function DrawLine3D(entity1, entity2)
	Line(EntityX(entity1), EntityY(entity1), EntityX(entity2), EntityY(entity2))
End Function

Function PositionHovering()
	If(hoveringEntity) Then
		CameraPick(cam,msx,msy)
		PositionEntity(hoveringEntity,Int(PickedX()),EntityY(hoveringEntity),Int(PickedZ()))
		
		;rotate it
		If(mszOld<>msz) Then RotateEntity(hoveringEntity,0,msz*15,0)
		
		;place it
		If(gui\mh1) Then
			gui\mh1 = 0 : FlushMouse()
			Select hoveringCategory
				Case "buildings"
					For b.Building = Each Building
						If(b\entity = hoveringEntity) Then
							If(Not(KeyDown(GetInt("key","placeMany")))) Then
								hoveringName = "" : hoveringCategory = "" : hoveringEntity = 0
							Else
								Local newB.Building = New Building
								newB\id = buildingId : buildingId=buildingId+1
								newB\entity = CopyEntity(hoveringEntity)
								hoveringEntity = newB\entity
								newB\name = hoveringName
								newB\loadonly = False
							EndIf
							EntityPickMode(b\entity,2)
							Exit
						EndIf
					Next
				Case "vehicles"
					For v.Vehicle = Each Vehicle
						If(v\entity = hoveringEntity) Then
							If(Not(KeyDown(GetInt("key","placeMany")))) Then
								hoveringName = "" : hoveringCategory = "" : hoveringEntity = 0
							Else
								Local newV.Vehicle = New Vehicle
								newV\id = vehicleId : vehicleId=vehicleId+1
								newV\entity = CopyEntity(hoveringEntity)
								hoveringEntity = newV\entity
								newV\name = hoveringName
								newV\loadonly = False
							EndIf
							EntityPickMode(v\entity,2)
							Exit
						EndIf
					Next
				Case "humans"
					For h.Human = Each Human
						If(h\entity = hoveringEntity) Then
							If(Not(KeyDown(GetInt("key","placeMany")))) Then
								hoveringName = "" : hoveringCategory = "" : hoveringEntity = 0
							Else
								Local newH.Human = New Human
								newH\id = humanId : humanId=humanId+1
								newH\entity = CopyEntity(hoveringEntity)
								hoveringEntity = newH\entity
								newH\name = hoveringName
								newH\loadonly = False
							EndIf
							EntityPickMode(h\entity,2)
							Exit
						EndIf
					Next
				Case "items"
					For i.Item = Each Item
						If(i\entity = hoveringEntity) Then
							If(Not(KeyDown(GetInt("key","placeMany")))) Then
								hoveringName = "" : hoveringCategory = "" : hoveringEntity = 0
							Else
								Local newI.Item = New Item
								newI\id = itemId : itemId=itemId+1
								newI\entity = CopyEntity(hoveringEntity)
								hoveringEntity = newI\entity
								newI\name = hoveringName
								newI\loadonly = False
							EndIf
							EntityPickMode(i\entity,2)
							Exit
						EndIf
					Next
				Case "triggers"
					For t.Trigger = Each Trigger
						If(t\entity = hoveringEntity) Then
							If(Not(KeyDown(GetInt("key","placeMany")))) Then
								hoveringName = "" : hoveringCategory = "" : hoveringEntity = 0
							Else
								Local newT.Trigger = New Trigger
								newT\id = triggerId : triggerId=triggerId+1
								newT\entity = CopyEntity(hoveringEntity)
								hoveringEntity = newT\entity
								newT\name = hoveringName
								newT\loadonly = False
							EndIf
							EntityPickMode(t\entity,2)
							Exit
						EndIf
					Next
			End Select
		EndIf
		
	EndIf
	
	;delete it
	If(KeyHit(211)) Then
		If(hoveringEntity) Then
			Select hoveringCategory
				Case "buildings"
					For b.Building = Each Building
						If(Not(b\loadonly)) Then
							If(b\entity = hoveringEntity) Then
								Delete b
							EndIf
						EndIf
					Next
				Case "vehicles"
					For v.Vehicle = Each Vehicle
						If(Not(loadonly)) Then
							If(v\entity = hoveringEntity) Then
								Delete v
							EndIf
						EndIf
					Next
				Case "humans"
					For h.Human = Each Human
						If(Not(h\loadonly)) Then
							If(h\entity = hoveringEntity) Then
								Delete h
							EndIf
						EndIf
					Next
				Case "items"
					For i.Item = Each Item
						If(Not(i\loadonly)) Then
							If(i\entity = hoveringEntity) Then
								Delete i
							EndIf
						EndIf
					Next
				Case "triggers"
					For t.Trigger = Each Trigger
						If(Not(t\loadonly)) Then
							If(t\entity = hoveringEntity) Then
								Delete t
							EndIf
						EndIf
					Next
			End Select
			hoveringName = "" : hoveringCategory = ""
			FreeEntity(hoveringEntity) : hoveringEntity = 0
		EndIf
	EndIf
End Function

Function Pick()
	If(msx>mainEditorWindowWidth) Then
		If(editBuildingWindow=0 And editVehicleWindow=0 And editHumanWindow=0 And editItemWindow=0 And editTriggerWindow=0) Then
			If(gui\mh1) Then
				Local objPicked = CameraPick(cam,msx,msy)
				;building?
				For b.Building = Each Building
					If(objPicked = b\entity) Then
						hoveringName = b\name
						hoveringCategory = "buildings"
						hoveringEntity = b\entity
						gui\mh1 = 0
						Return
					EndIf
				Next
				
				;vehicle?
				For v.Vehicle = Each Vehicle
					If(objPicked = v\entity) Then
						hoveringName = v\name
						hoveringCategory = "vehicles"
						hoveringEntity = v\entity
						gui\mh1 = 0
						Return
					EndIf
				Next
				
				;human?
				For h.Human = Each Human
					If(objPicked = h\entity) Then
						hoveringName = h\name
						hoveringCategory = "humans"
						hoveringEntity = h\entity
						gui\mh1 = 0
						Return
					EndIf
				Next
				
				;item?
				For i.Item = Each Item
					If(objPicked = i\entity) Then
						hoveringName = i\name
						hoveringCategory = "items"
						hoveringEntity = i\entity
						gui\mh1 = 0
						Return
					EndIf
				Next
				
				;trigger?
				For t.Trigger = Each Trigger
					If(objPicked = t\entity) Then
						hoveringName = t\name
						hoveringCategory = "triggers"
						hoveringEntity = t\entity
						gui\mh1 = 0
						Return
					EndIf
				Next
			EndIf
		EndIf
	EndIf
End Function

Function MoveOnMap()
	If(KeyDown(GetInt("key","moveUp"))) Then 
		If(KeyDown(GetInt("key","moveFast"))) Then
			MoveEntity(cam,0,1.5,0)
		Else
			MoveEntity(cam,0,0.5,0)
		EndIf
	EndIf
	If(KeyDown(GetInt("key","moveDown"))) Then 
		If(KeyDown(GetInt("key","moveFast"))) Then
			MoveEntity(cam,0,-1.5,0)
		Else
			MoveEntity(cam,0,-0.5,0)
		EndIf
	EndIf
	If(KeyDown(GetInt("key","moveLeft"))) Then 
		If(KeyDown(GetInt("key","moveFast"))) Then
			MoveEntity(cam,-1.5,0,0)
		Else
			MoveEntity(cam,-0.5,0,0)
		EndIf
	EndIf
	If(KeyDown(GetInt("key","moveRight"))) Then 
		If(KeyDown(GetInt("key","moveFast"))) Then
			MoveEntity(cam,1.5,0,0)
		Else
			MoveEntity(cam,0.5,0,0)
		EndIf
	EndIf
	
	If((mszSpeed <> 0) And (hoveringEntity = 0)) Then
		If(KeyDown(GetInt("key","moveFast"))) Then
			MoveEntity(cam,0,0,mszSpeed*5)
		Else
			MoveEntity(cam,0,0,mszSpeed)
		EndIf
	EndIf
End Function

Function InitAll(fileName$, category$="find automatically")
	Local currentLine$, delimiter, currentCategory$, countLine=0
	Local v.Var
	
	fileStream = ReadFile(fileName)
	
	;find category
	While(Not(Eof(fileStream)))
		countLine=countLine+1
		currentLine = ReadLine(fileStream)
		If(category$="find automatically") Then
			If(Left(currentLine,1) = "[") Then
				currentCategory = Mid(currentLine,2,Len(currentLine)-2)
			ElseIf(Trim(currentLine) = "") Then
				;do nothing
			Else
				delimiter = Instr(currentLine,"=")
				v.Var = New Var
				v\category = currentCategory
				v\name = Left(currentLine,delimiter-1)
				v\value = Mid(currentLine,delimiter+1)
				;DebugLog("created var ["+v\category+"] "+v\name)
			EndIf
		Else
			v.Var = New Var
			v\category = category
			v\name=Str(countLine)
			v\value=currentLine
		EndIf
	Wend
	
	CloseFile(fileStream)
End Function

Function DeleteVars(category$="all")
	For v.Var = Each Var
		If((v\category = category) Or (category="all")) Then
			;DebugLog("deleted var "+v\name)
			Delete v
		EndIf
	Next
End Function

Function GetInt(category$, name$)
	For v.Var = Each Var
		If((v\category = category) And (v\name = name)) Then
			Return Int(v\value)
		EndIf
	Next
	RuntimeError("Integer not found: category="+category+", name="+intName)
End Function

Function GetString$(category$, name$)
	For v.Var = Each Var
		If((v\category = category) And (v\name = name)) Then
			Return v\value
		EndIf
	Next
	RuntimeError("String not found: category="+category+", name="+strName)
End Function

Function GetFloat#(category$, name$)
	For v.Var = Each Var
		If((v\category = category) And (v\name = name)) Then
			Return Float(v\value)
		EndIf
	Next
	RuntimeError("Float not found: category="+category+", name="+floatName)
End Function

Function MoveLocalPlayer()
	Local playerIsMoving = False
	
	;forward / backward / left / right
	If(KeyDown(GetInt("key","left"))) Then
		If(localPlayer\inVehicle) Then 
			For v.Vehicle = Each Vehicle
				If(localPlayer\inVehicle = v\entity) Then
					If(v\speed <> 0) Then
						RotateEntity(v\entity,0,EntityYaw(v\entity)+GetFloat(v\name,"steer")*(v\speed/100),0)
					EndIf
				EndIf
			Next
		Else
			TurnEntity(localPlayer\entity,0,4,0)
		EndIf
		playerIsMoving = True
	EndIf
	If(KeyDown(GetInt("key","right"))) Then
		If(localPlayer\inVehicle) Then 
			For v.Vehicle = Each Vehicle
				If(localPlayer\inVehicle = v\entity) Then
					If(v\speed <> 0) Then
						RotateEntity(v\entity,0,EntityYaw(v\entity)-GetFloat(v\name,"steer")*(v\speed/100),0)
					EndIf
				EndIf
			Next
		Else
			TurnEntity(localPlayer\entity,0,-4,0)
			playerIsMoving = True
		EndIf
	EndIf
	If(KeyDown(GetInt("key","forward"))) Then
		If(localPlayer\inVehicle) Then
			For v.Vehicle = Each Vehicle
				If(localPlayer\inVehicle = v\entity) Then
					Local oldSpeed = v\speed
					If(v\speed < GetInt(v\name, "gear_1_speed")) Then
						v\speed=v\speed+(GetFloat(v\name,"gear_1_accelerate")*(60.0/fps))
					ElseIf(v\speed < GetInt(v\name, "gear_2_speed")) Then
						v\speed=v\speed+(GetFloat(v\name,"gear_2_accelerate")*(60.0/fps))
					ElseIf((v\speed < GetInt(v\name, "gear_3_speed")) And (v\energy / v\maxEnergy >= 0.1)) Then
						v\speed=v\speed+(GetFloat(v\name,"gear_3_accelerate")*(60.0/fps))
					ElseIf((v\speed < GetInt(v\name, "gear_4_speed")) And (v\energy / v\maxEnergy >= 0.3)) Then
						v\speed=v\speed+(GetFloat(v\name,"gear_4_accelerate")*(60.0/fps))
					ElseIf((v\speed < GetInt(v\name, "gear_5_speed")) And (v\energy / v\maxEnergy >= 0.5)) Then
						v\speed=v\speed+(GetFloat(v\name,"gear_5_accelerate")*(60.0/fps))
					EndIf
					v\deltaSpeed = v\speed - oldSpeed
					;DebugLog("delta speed: "+v\deltaSpeed)
					Exit
				EndIf
			Next
		Else
			If(Not(dontMove)) Then
				MoveEntity(localPlayer\entity,0,0,-0.15*(60.0/fps))
				localPlayerAnimation = "forward"
			EndIf
		EndIf
		playerIsMoving = True
	EndIf
	If(KeyDown(GetInt("key","backward"))) Then
		If(localPlayer\inVehicle) Then 
			For v.Vehicle = Each Vehicle
				If(localPlayer\inVehicle = v\entity) Then
					If(v\speed>0) Then
						v\speed=v\speed-GetFloat(v\name,"brake")
						DebugLog("braking")
					Else
						;vehicle can drive backwards as fast as in 1st gear
						If(v\speed > (0-GetInt(v\name, "gear_1_speed"))) Then
							v\speed=v\speed-GetFloat(v\name,"gear_1_accelerate")
						EndIf
					EndIf
					Exit
				EndIf
			Next
		Else
			If(Not(dontMove)) Then
				MoveEntity(localPlayer\entity,0,0,0.2)
			EndIf
		EndIf
		playerIsMoving = True
	EndIf
	
	;jump
;	If(KeyDown(GetInt("key","jump"))) Then
;		singlePlayer\jumping = 1
;		singlePlayerAnimation = "jumping"
;	EndIf
	
	;jumping process
;	If(singlePlayer\jumping = 1) Then
;		
;	EndIf
	
	;enter a vehicle
	If(KeyHit(GetInt("key","enter_vehicle"))) Then
		;on foot
		If(Not(localPlayer\inVehicle)) Then
			Local entity = FindNearestVehicle()
			If(entity) Then
				localPlayer\inVehicle = entity
				HideEntity(localPlayer\entity)
			EndIf
		;already in vehicle
		Else
			;DebugLog("set plaer to x="+EntityX(localPlayer\inVehicle)+", z="+EntityZ(localPlayer\inVehicle))
			PositionEntity(localPlayer\entity,EntityX(localPlayer\inVehicle),HUMAN_Y,EntityZ(localPlayer\inVehicle))
			RotateEntity(localPlayer\entity,0,EntityYaw(localPlayer\inVehicle),0)
			MoveEntity(localPlayer\entity,MeshWidth(localPlayer\inVehicle),0,0)
			localPlayer\inVehicle = 0
			ShowEntity(localPlayer\entity)
		EndIf
	EndIf
	
	If(Not(playerIsMoving)) Then
		localPlayerAnimation = "none"
	EndIf
	
	If(localPlayer\inVehicle) Then
		For v.Vehicle = Each Vehicle
			If(localPlayer\inVehicle = v\entity) Then
				PositionEntity(cam,EntityX(v\entity),EntityY(cam),EntityZ(v\entity))
				PositionEntity(listener,EntityX(v\entity),1,EntityZ(v\entity))
				Exit
			EndIf
		Next
	Else
		PositionEntity(cam,EntityX(localPlayer\entity),EntityY(cam),EntityZ(localPlayer\entity))
		PositionEntity(listener,EntityX(localPlayer\entity),1,EntityZ(localPlayer\entity))
	EndIf
	
	;fart
	If(KeyHit(GetInt("key","fart"))) Then
		If(Not(localPlayer\inVehicle)) Then
			If(Not(ChannelPlaying(fartChannel))) Then
				fartChannel=EmitSound(fart(Rnd(0,3)),localPlayer\entity)
				ChannelVolume(fartChannel,GetFloat("sound","volumeEffects"))
			EndIf
		EndIf
	EndIf
End Function

Function AnimateLocalPlayer()
	Select localPlayerAnimation
		Case "none"
			EntityTexture(localPlayer\entity,localPlayer\texture,0)
		Case "forward"
			EntityTexture(localPlayer\entity,localPlayer\texture,((MilliSecs() Mod 600) / 40))
		Case "jumping"
			
	End Select
End Function

Function HUD()
	SetFont(hudFont)
	Local lifeFrame = (((Float(localPlayer\energy) / localPlayer\maxEnergy)*100)/5)-1
	If(lifeFrame<0) Then lifeFrame=0
	DrawImage(hudLife,30,30,lifeFrame)
	Text(30+16,70,localPlayer\energy+"%",30+16)
	
	For hImage.HudWeaponImage = Each HudWeaponImage
		If(hImage\id = localPlayerWeapon) Then
			;DebugLog("singlePlayerWeapon: "+singlePlayerWeapon)
			If(hImage\image) Then DrawImage(hImage\image,210,25)
			;select weapon
			For ws.WeaponShoot = Each WeaponShoot
				If(ws\id = localPlayerWeapon) Then
				;possessed by player
					For possess.WeaponPossessedByHuman = Each WeaponPossessedByHuman
						If(possess\human = Handle(localPlayer) And possess\name = ws\name) Then
							Text(235,70,possess\ammo,235)
							Exit
						EndIf
					Next
				EndIf
			Next
			Exit
		EndIf
	Next
	
	DrawImage(hudCash,120,20)
	Text(135,70,"$ "+singlePlayerCash,135)
End Function

Function MoveVehicles()
	For v.Vehicle = Each Vehicle
		If(Not(v\loadonly)) Then
			;drive slowlier when damaged
			Local damageFactor# = 1
			If(v\energy / v\maxEnergy < 0.5) Then
				damageFactor# = 0.7
			ElseIf(v\energy / v\maxEnergy < 0.3) Then
				damageFactor# = 0.5
			ElseIf(v\energy / v\maxEnergy < 0.1) Then
				damageFactor# = 0.3
			EndIf
			
			If(v\speed <> 0) Then
				;calculate distance
				Local distance# = (0.5 * (v\deltaSpeed/(1.0/fps)) * Power(1.0/fps) + (v\speed - v\deltaSpeed) * (1.0/fps)) / DISTANCE_FACTOR
				DebugLog(distance)
				MoveEntity(v\entity,0,0,-distance)
				
				;calculate friction
				Local movingPositive = Sgn(v\speed)
				v\speed=v\speed-GetFloat(v\name,"friction")*Sgn(v\speed)
				If(((movingPositive=1) And (v\speed<0)) Or ((movingPositive=-1) And (v\speed>0))) Then v\speed = 0
			EndIf
			
			;sound
			If(v\energy>0) Then
				If(Not(ChannelPlaying(v\motorChannel))) Then v\motorChannel = EmitSound(carMotorSfx,v\entity)
				If(v\speed < 0) Then
					ChannelPitch(v\motorChannel,44100-(v\speed*MOTOR_PITCH_MULTIPLICATOR))
				ElseIf(v\speed < GetFloat(v\name, "gear_1_speed")) Then
					ChannelPitch(v\motorChannel,44100+(v\speed*MOTOR_PITCH_MULTIPLICATOR))
				ElseIf(v\speed < GetFloat(v\name, "gear_2_speed")) Then
					ChannelPitch(v\motorChannel,44100+(v\speed-GetFloat(v\name, "gear_1_speed")+GetFloat(v\name, "gear_1_speed")/2)*MOTOR_PITCH_MULTIPLICATOR)
				ElseIf(v\speed < GetFloat(v\name, "gear_3_speed")) Then
					ChannelPitch(v\motorChannel,44100+(v\speed-GetFloat(v\name, "gear_2_speed")+GetFloat(v\name, "gear_2_speed")/2)*MOTOR_PITCH_MULTIPLICATOR)
				ElseIf(v\speed < GetFloat(v\name, "gear_4_speed")) Then
					ChannelPitch(v\motorChannel,44100+(v\speed-GetFloat(v\name, "gear_3_speed")+GetFloat(v\name, "gear_3_speed")/2)*MOTOR_PITCH_MULTIPLICATOR)
				ElseIf(v\speed < GetFloat(v\name, "gear_5_speed")-10) Then
					ChannelPitch(v\motorChannel,44100+(v\speed-GetFloat(v\name, "gear_4_speed")+GetFloat(v\name, "gear_4_speed")/2)*MOTOR_PITCH_MULTIPLICATOR)
				Else
					;debug
				EndIf
				ChannelVolume(v\motorChannel,GetFloat("sound","volumeAmbient"))
			EndIf
			
			;if singleplayer in that car: move him too
			If(v\entity = localPlayer\inVehicle) Then
				PositionEntity(localPlayer\entity,EntityX(v\entity),10,EntityZ(v\entity))
			EndIf
		EndIf
	Next
End Function

Function VehicleEmitters()
	For v.Vehicle = Each Vehicle
		If(Not(v\loadonly)) Then
			If((v\energy / v\maxEnergy < 0.5) And (v\damageEmitterLevel < 1)) Then
				FreeEmitter(v\damageEmitter)
				SetEmitter(v\damageEmitter, vehicleBurn1) : v\damageEmitterLevel = 1
			ElseIf((v\energy / v\maxEnergy < 0.3) And (v\damageEmitterLevel < 2)) Then
				FreeEmitter(v\damageEmitter)
				SetEmitter(v\damageEmitter, vehicleBurn2) : v\damageEmitterLevel = 2
			ElseIf((v\energy / v\maxEnergy < 0.1) And (v\damageEmitterLevel < 3)) Then
				FreeEmitter(v\damageEmitter)
				SetEmitter(v\damageEmitter, vehicleBurn3) : v\damageEmitterLevel = 3
			ElseIf((v\energy <= 0) And (v\damageEmitterLevel < 4)) Then
				FreeEmitter(v\damageEmitter)
				SetEmitter(v\damageEmitter, vehicleExplode) : v\damageEmitterLevel = 4
				DestroyVehicleLight(v\entity)
				EntityTexture(v\entity,carWreck)
				StopChannel(v\motorChannel)
				EmitSound(vehicleExplodeSfx,v\entity)
			EndIf
		EndIf
	Next
End Function

Function VehicleActions()
	If(localPlayer\inVehicle) Then
		;light on / off
		If(KeyHit(GetInt("key","light"))) Then
			For vl.VehicleLight = Each VehicleLight
				If(vl\vehicleEntity = localPlayer\inVehicle) Then
					If(vl\kind = "headlight") Then
						vl\on = 1-vl\on
					EndIf
				EndIf
			Next
		EndIf
		
		;horn
		If(KeyDown(GetInt("key","horn"))) Then
			For v.Vehicle = Each Vehicle
				If(localPlayer\inVehicle = v\entity) Then
					;siren lights
					Select v\category
						Case "police car"
							For vl.VehicleLight = Each VehicleLight
								If((vl\vehicleEntity = v\entity) And (vl\kind = "siren")) Then
									If(vl\which = "siren1") Then
										vl\on=(MilliSecs() Mod 800 < 400)
									Else
										vl\on=1-(MilliSecs() Mod 800 < 400)
									EndIf
								EndIf
							Next
					End Select
					If(Not(ChannelPlaying(v\hornChannel))) Then
						v\hornChannel=EmitSound(horn(GetInt(v\name,"horn")),v\entity)
						ChannelVolume(v\hornChannel,GetFloat("sound","volumeEffects"))
					EndIf
				EndIf
			Next
		Else
			For v.Vehicle = Each Vehicle
				If(localPlayer\inVehicle = v\entity) Then
					;siren lights
					Select v\category
						Case "police car"
							For vl.VehicleLight = Each VehicleLight
								If((vl\vehicleEntity = v\entity) And (vl\kind = "siren")) Then
									vl\on=0
								EndIf
							Next
					End Select
					StopChannel(v\hornChannel)
				EndIf
			Next
		EndIf
	EndIf
End Function

Function UpdateLights()
	For vl.VehicleLight = Each VehicleLight
		If(vl\on) Then
			LightRange(vl\light,1)
			EntityFX(vl\bulb,1)
		Else
			LightRange(vl\light,0)
			EntityFX(vl\bulb,0)
		EndIf
	Next
End Function

Function FindNearestVehicle()
	Local minDistance = MAX_DISTANCE_TO_NEXT_VEHICLE
	Local closestVehicle = 0
	
	For v.Vehicle = Each Vehicle
		If(Not(v\loadonly)) Then
			If(v\energy>0) Then
				Local distance = GetEntityDistance(v\entity,localPlayer\entity)
				If(distance < minDistance) Then
					minDistance = distance
					closestVehicle = v\entity
				EndIf
			EndIf
		EndIf
	Next
	
	Return closestVehicle
End Function

Function SetEnergy()
	;single player is a human as well
	For h.Human = Each Human
		If(Not(h\loadonly)) Then
			;die
			If(h\energy <= 0) Then
				;debug: remove
				FreeEntity(h\entity)
				Delete h
			EndIf
			
;			If(h\entity = singlePlayer\entity) Then
;				;h\energy=h\energy-0.6
;				If(h\energy <= 0) Then RuntimeError("You are dead")
;			EndIf
		EndIf
	Next
	
	For v.Vehicle = Each Vehicle
		;TODO
	Next
End Function

Function RotateItems()
	If(rotateItemTimer < MilliSecs()) Then
		rotateItemTimer=MilliSecs()+250
		For i.Item = Each Item
			If(Not(i\loadonly)) Then
				TurnEntity(i\entity,0,-45,0)
			EndIf
		Next
	EndIf
End Function

Function Collect()
	Local playerCollider
	If(localPlayer\inVehicle) Then
		playerCollider = localPlayer\inVehicle
	Else
		playerCollider = localPlayer\colSphere
	EndIf
	
	;find item and collect it
	For i.Item = Each Item
		If(Not(i\loadonly)) Then
			If(MeshesIntersect(playerCollider,i\entity)) Then
				;weapon?
				For ws.WeaponShoot = Each WeaponShoot
					If(ws\name = i\name) Then
						;match weapon possessed by single player
						For possess.WeaponPossessedByHuman = Each WeaponPossessedByHuman
							DebugLog("human handle:" + possess\human)
							If(possess\human = Handle(localPlayer) And possess\name = i\name) Then
								possess\ammo = possess\ammo + i\value
								;todo
								DebugLog("collected "+possess\ammo+" bullets for weapon '"+ws\name+"'")
								Exit
							EndIf
						Next
						Exit
					EndIf
				Next
				
				;cash?
				If(i\name = "cash") Then
					singlePlayerCash=singlePlayerCash+i\value
				EndIf
				
				;delete item
				FreeEntity(i\entity)
				Delete i
				
				Return 1
			EndIf
		EndIf
	Next
	
	Return 0
End Function

Function Attack()
	If(KeyDown(GetInt("key","attack"))) Then
		;select weapon
		For ws.WeaponShoot = Each WeaponShoot
			If(ws\id = localPlayerWeapon) Then
				;possessed by player
				For possess.WeaponPossessedByHuman = Each WeaponPossessedByHuman
					If(possess\human = Handle(localPlayer) And possess\name = ws\name And possess\ammo <> 0) Then
						If(MilliSecs() > possess\nextShot) Then
							;shoot
							possess\nextShot = MilliSecs() + ws\shot_interval
							Local newBullet.Bullet = New Bullet
							newBullet\entity = CreateCube() : EntityColor(newBullet\entity,255,255,0) ;change?
							ScaleEntity(newBullet\entity,0.04,1,0.055)
							PositionEntity(newBullet\entity,EntityX(localPlayer\entity),EntityY(localPlayer\entity),EntityZ(localPlayer\entity))
							RotateEntity(newBullet\entity,0,EntityYaw(localPlayer\entity),0)
							MoveEntity(newBullet\entity,0,0,-0.6)
							EntityFX(newBullet\entity,1)
							newBullet\speed = ws\bulletSpeed
							newBullet\lifeTime = ws\bulletLifeTime
							newBullet\damage = ws\damage
							possess\ammo=possess\ammo-1
							
							;weapon sound
							For snd.WeaponSound = Each WeaponSound
								If(snd\id = ws\id) Then
									channelEffects = EmitSound(snd\sound,newBullet\entity)
									ChannelVolume(channelEffects,GetFloat("sound","volumeEffects"))
								EndIf
							Next
							
							;last bullet?
							If(possess\ammo = 0) Then
								SwitchWeapon("next")
							EndIf
							
							Return
						EndIf
					EndIf
				Next
			EndIf
		Next
	EndIf
End Function

Function MoveBullets()
	For b.Bullet = Each Bullet
		MoveEntity(b\entity,0,0,-b\speed)
		DebugLog("moving bullet")
		b\lifetime=b\lifetime-1
		
		;hit a vehicle?
		For v.Vehicle = Each Vehicle
			If(Not(v\loadonly)) Then
				If(MeshesIntersect(b\entity,v\entity)) Then
					;damage vehicle
					v\energy=v\energy-b\damage
					DebugLog("energy left: "+v\energy)
					
					;play hit sound
					EmitSound(bulletVehicleSfx,b\entity)
					
					;delete bullet
					FreeEntity(b\entity)
					Delete b
					Return
				EndIf
			EndIf
		Next
		
		;hit a human?
		For h.Human = Each Human
			If(Not(h\loadonly)) Then
				If(MeshesIntersect(b\entity,h\entity)) Then
					;damage human
					h\energy=h\energy-b\damage
					
					;play sound?
					;TODO
					
					;delete bullet
					FreeEntity(b\entity)
					Delete b
					Return
				EndIf
			EndIf
		Next
		
		If(b\lifetime <= 0) Then
			;delete bullet
			FreeEntity(b\entity)
			Delete b
		EndIf
	Next
End Function

Function SwitchWeapon(param$="")
	Local switchToNext = KeyHit(GetInt("key","next_weapon"))
	Local switchToPrevious = KeyHit(GetInt("key","previous_weapon"))
	
	If(switchToNext Or switchToPrevious Or param<>"") Then
		Repeat
			If(switchToNext Or param="next") Then
				If(localPlayerWeapon<localPlayerMaxWeaponId) Then
					localPlayerWeapon=localPlayerWeapon+1
				Else
					localPlayerWeapon=0
					DebugLog("switched to NO weapon")
					Return
				EndIf
			ElseIf(switchToPrevious Or param="previous") Then
				If(localPlayerWeapon>0) Then
					localPlayerWeapon=localPlayerWeapon-1
					If(localPlayerWeapon=0) Then
						DebugLog("switched to NO weapon")
						Return
					EndIf
				Else
					localPlayerWeapon=localPlayerMaxWeaponId
				EndIf
			EndIf
			
			;match each weapon type
			For ws.WeaponShoot = Each WeaponShoot
				;check if weapon has the correct id
				If(ws\id = localPlayerWeapon) Then
					;match if weapon possessed by player
					For possess.WeaponPossessedByHuman = Each WeaponPossessedByHuman
						If((possess\human = Handle(localPlayer)) And (possess\ammo<>0) And (ws\name = possess\name)) Then
							;add gui info, like name, ammo etc.
							DebugLog("switched to weapon '"+possess\name+"'")
							Return
						EndIf
					Next
				EndIf
			Next
		Forever
	EndIf
End Function

Function Collide()
	;human collisions
	For h.Human = Each Human
		If(Not(h\loadonly)) Then
			;collide with building
			For b.Building = Each Building
				If(Not(b\loadonly)) Then
					If(EntityDistance(h\colSphere,b\entity) < HUMAN_BUILDING_COLLISION_DISTANCE) Then
						If(MeshesIntersect(h\colSphere,b\entity)) Then
							MoveEntity(h\entity,0,0,0.15*(60.0/fps))
							Exit
						EndIf
					EndIf
				EndIf
			Next
			
			;collide with vehicle
			For v.Vehicle = Each Vehicle
				If(Not(v\loadonly)) Then
					If(EntityDistance(h\colSphere,v\entity) < HUMAN_VEHICLE_COLLISION_DISTANCE) Then
						If(MeshesIntersect(h\colSphere,v\entity)) Then
							;car drives slowly: stop player
							If(v\speed < VEHICLE_OVERRUN_SPEED) Then
								MoveEntity(h\entity,0,0,0.15*(60.0/fps))
							;overrun
							Else
								h\energy = 0
								EmitSound(overrunSfx,h\entity)
							EndIf
							Exit
						EndIf
					EndIf
				EndIf
			Next
		EndIf
	Next
	
	;vehicle collisions
	For v.Vehicle = Each Vehicle
		If(Not(v\loadonly)) Then
			;collide with building
			For b.Building = Each Building
				If(Not(b\loadonly)) Then
					If(EntityDistance(v\entity,b\entity) < VEHICLE_BUILDING_COLLISION_DISTANCE) Then
						If(MeshesIntersect(v\entity,b\entity)) Then
							;check if headlight is broken
							If(Abs(v\speed) >= HEADLIGHT_BREAK_SPEED) Then
								For vl.VehicleLight = Each VehicleLight
									If(vl\vehicleEntity = v\entity) Then
										If(MeshesIntersect(vl\colBulb,b\entity)) Then
											channelEffects = EmitSound(glassBreakSfx,vl\bulb)
											ChannelVolume(channelEffects,GetFloat("sound","volumeEffects"))
											FreeEntity(vl\light)
											FreeEntity(vl\bulb)
											Delete vl
										EndIf
									EndIf
								Next
							EndIf
							If(v\speed>0) Then
								MoveEntity(v\entity,0,0,0.08)
							ElseIf(v\speed<0) Then
								MoveEntity(v\entity,0,0,-0.08)
							EndIf
							v\speed = 0
							Exit
						EndIf
					EndIf
				EndIf
			Next
		EndIf
	Next
End Function

Function DeleteAllOnMap(inEditor=True)
	For oldB.Building = Each Building
		If(Not(oldB\loadonly)) Then
			If(oldB\entity) Then FreeEntity(oldB\entity)
			Delete oldB
		EndIf
	Next
	For oldV.Vehicle = Each Vehicle
		If(Not(oldV\loadonly)) Then
			If(Not(inEditor)) Then
				FreeEntity(oldV\entity)
			EndIf
			StopChannel(oldV\hornChannel) : StopChannel(oldV\motorChannel)
			Delete oldV
		EndIf
	Next
	For oldH.Human = Each Human
		If(Not(oldH\loadonly)) Then
			If(Not(inEditor)) Then
				FreeEntity(oldH\entity)
			EndIf
			Delete oldH
		EndIf
	Next
	For oldI.Item = Each Item
		If(Not(oldI\loadonly)) Then
			If(oldI\entity) Then FreeEntity(oldI\entity)
			Delete oldI
		EndIf
	Next
	For oldT.Trigger = Each Trigger
		If(Not(oldT\loadonly)) Then
			If(oldT\entity) Then FreeEntity(oldT\entity)
			Delete oldT
		EndIf
	Next
	For oldBullet.Bullet = Each Bullet
		Delete oldBullet
	Next
End Function

Function DeleteLoadOnlys()
	For b.Building = Each Building
		Delete b
	Next
	
	For v.Vehicle = Each Vehicle
		Delete v
	Next
	
	For vl.VehicleLight = Each VehicleLight
		Delete vl
	Next
	
	For h.Human = Each Human
		Delete h
	Next
	
	For i.Item = Each Item
		Delete i
	Next
	
	For t.Trigger = Each Trigger
		Delete t
	Next
End Function

Function CreateVehicleLights(vehicleName$, vehicleEntity)
	Local category$ = GetString(vehicleName,"category")
	Local numOfLights = GetInt(category,"lights")
	For i=1 To numOfLights
		Local newLight.VehicleLight = New VehicleLight
		newLight\which = GetString(category,"name"+i)
		newLight\kind = GetString(category,"kind"+i)
		newLight\vehicleEntity = vehicleEntity
		newLight\light = CreateLight(3)
		If(newLight\kind = "headlight") Then
			PositionEntity(newLight\light,EntityX(vehicleEntity)+(GetFloat(vehicleName,"scale_x")*GetInt(category,"sgnX"+i))+GetFloat(category,"x"+i),EntityY(vehicleEntity),EntityZ(vehicleEntity)+(GetFloat(vehicleName,"scale_z")*GetInt(category,"sgnZ"+i))+GetFloat(category,"z"+i))
		ElseIf(newLight\kind = "siren") Then
			PositionEntity(newLight\light,EntityX(vehicleEntity)+GetFloat(category,"x"+i),EntityY(vehicleEntity),EntityZ(vehicleEntity)+GetFloat(category,"z"+i))
		EndIf
		LightColor(newLight\light,GetInt(category,"red"+i),GetInt(category,"green"+i),GetInt(category,"blue"+i))
		LightRange(newLight\light,0)
		EntityParent(newLight\light,vehicleEntity)
		newLight\bulb = CreateSphere(3) : ScaleEntity(newLight\bulb,0.15,0.15,0.1)
		If(newLight\kind = "headlight") Then
			PositionEntity(newLight\bulb,EntityX(vehicleEntity)+(GetFloat(vehicleName,"scale_x")*GetInt(category,"sgnX"+i))+GetFloat(category,"x"+i),EntityY(vehicleEntity),EntityZ(vehicleEntity)+(GetFloat(vehicleName,"scale_z")*GetInt(category,"sgnZ"+i))+GetFloat(category,"z"+i))
		ElseIf(newLight\kind = "siren") Then
			PositionEntity(newLight\bulb,EntityX(vehicleEntity)+GetFloat(category,"x"+i),EntityY(vehicleEntity),EntityZ(vehicleEntity)+GetFloat(category,"z"+i))
		EndIf
		newLight\colBulb = CreateSphere(3) : PositionEntity(newLight\colBulb,EntityX(newLight\bulb),EntityY(newLight\bulb),EntityZ(newLight\bulb)) : ScaleEntity(newLight\colBulb,0.5,0.5,0.5)
		EntityParent(newLight\colBulb,newLight\bulb) : EntityAlpha(newLight\colBulb,0)
		EntityColor(newLight\bulb,GetInt(category,"red"+i),GetInt(category,"green"+i),GetInt(category,"blue"+i))
		EntityParent(newLight\bulb,vehicleEntity)
		newLight\on = 0
	Next
End Function

Function DestroyVehicleLight(vehicleEntity, which$="all")
	For vl.VehicleLight = Each VehicleLight
		If(vl\vehicleEntity = vehicleEntity) Then
			If((vl\which = which) Or (which="all")) Then
				FreeEntity(vl\bulb)
				FreeEntity(vl\light)
				Delete vl
			EndIf
		EndIf
	Next
End Function

Function DayTime()
	If(gameClockTimer < MilliSecs()) Then
		gameClockTimer=MilliSecs()+100
		gameClock=gameClock+0.05
	EndIf
	
	If(gameClock>24) Then gameClock=0
	
	If(gameClock>=6 And gameClock<=8) Then
		AmbientLight(30+(gameClock-6)*45,30+(gameClock-6)*45,30+(gameClock-6)*45)
	ElseIf(gameClock>=8 And gameClock<=19) Then
		AmbientLight(128,128,128)
	ElseIf(gameClock>=19 And gameClock<=21) Then
		AmbientLight(128-(gameClock-19)*45,128-(gameClock-19)*45,128-(gameClock-19)*45)
	Else
		AmbientLight(30,30,30)
	EndIf
	
	
End Function

Function BackToMenu(inEditor=False)
	If(KeyHit(1)) Then
		Local goBack
		If(inEditor) Then
			goBack=GUI_MsgBox(GetString("backToMenuFromEditor","title"), GetString("backToMenuFromEditor","line1") + Chr(10) + GetString("backToMenuFromEditor","line2"), 2, GetInt("graphics","screenWidth")/2+150-275/2)
		Else
			goBack=GUI_MsgBox(GetString("backToMenu","title"), GetString("backToMenu","line1") + Chr(10) + GetString("backToMenu","line2"), 2)
		EndIf
		If(goBack=1) Then
			;reset everything (TODO)
			DeleteAllOnMap(False)
			map = ""
			If(inEditor) Then
				GUI_Message(mainEditorWindow, "close")
				Delete Each Behaviour
				InitGame()
			Else
				Delete Each WeaponPossessedByHuman
				singlePlayerCash = 0
				singlePlayerMaxWeaponId = 0
				singlePlayerWeapon = 0
			EndIf
			GUI_Message(menuWindow, "minimize") : GUI_Message(menuWindow, "bringtofront")
			section = "init menu"
		EndIf
	EndIf
End Function

Function Quit()
	If(KeyHit(1)) Then 
		Local doQuit = GUI_MsgBox(GetString("quit","title"), GetString("quit","line1") + Chr(10) + GetString("quit","line2"), 2)
		If(doQuit=1) Then
			SaveMenuSettings()
			GUI_FreeGUI()
			FreeParticles()
			End()
		EndIf
	EndIf
End Function


Type Var
	Field category$
	Field name$
	Field value$
End Type

Type Scroller
	Field name$
	Field id
	Field length
End Type

Type Thumbnail
	Field name$
	Field category$
	Field id
	Field y
End Type

Type Building
	Field id
	Field name$
	Field entity
	Field loadonly
End Type

Type Human
	Field id
	Field name$
	Field entity
	Field texture
	Field colSphere
	Field jumping		; 0 = not jumping, 1 = jumping up, -1 = jumping down
	Field inVehicle		; 0 = no vehicle, number = entity handle
	Field energy
	Field maxEnergy
	Field behaviour$
	Field loadonly
End Type

Type Vehicle
	Field id
	Field name$
	Field entity
	Field category$
	Field speed#
	Field deltaSpeed#
	Field energy#
	Field maxEnergy#
	Field loadonly
	Field motorChannel
	Field hornChannel
	Field damageEmitter, damageEmitterLevel
End Type

Type VehicleLight
	Field which$
	Field kind$
	Field light
	Field bulb
	Field colBulb
	Field vehicleEntity
	Field on
End Type

Type Item
	Field id
	Field name$
	Field entity
	Field category$		; e.g. "WeaponShoot", "Cash" etc.
	Field loadonly
	Field value			;how much of it can be collected (e.g. 10 ammo, 50 $)
End Type

Type Trigger
	Field id
	Field name$
	Field entity
	Field behaviour$
	Field loadonly
End Type

Type WeaponPossessedByHuman
	Field human			; which human possesses the weapon
	Field name$			; name of this weapon
	Field behaviour$	; e.g. "shoot", "throw" etc.
	Field ammo			; -1 = infinite
	Field nextShot		; -1 = no reload time
End Type

Type WeaponShoot
	Field id
	Field name$
	Field shot_interval
	Field ammo_per_magazine
	Field damage
	Field bulletSpeed#
	Field bulletLifeTime
End Type

Type WeaponThrow
	Field id
	Field name$
End Type

Type WeaponFlame
	Field id
	Field name$
End Type

Type WeaponRocket
	Field id
	Field name$
End Type

Type WeaponBeat
	Field id
	Field name$
End Type

Type WeaponSound
	Field id
	Field sound
End Type

Type HudWeaponImage
	Field id
	Field image
End Type

Type Bullet
	Field entity
	Field speed#
	Field lifeTime
	Field damage
End Type

Type Behaviour
	Field name$
	Field category$
End Type

Type ScriptLine
	Field behaviour$
	Field category$
	Field lineNr
	Field txt$
End Type

Type BehGuiVar
	Field objId
	Field category$
	Field edit
	Field label
End Type

Type BehInteger
	Field objId
	Field category$
	Field name$
	Field value
End Type

Type BehString
	Field objId
	Field category$
	Field name$
	Field value$
End Type

Type BehFloat
	Field objId
	Field category$
	Field name$
	Field value#
End Type

Type TempInteger
	Field name$
	Field value
End Type

Type TempString
	
End Type

Type TempFloat
	
End Type
;~IDEal Editor Parameters:
;~F#13D#15C#16D#20F#21C#22F#24D#256#2BD#331#396#3CD#3D7#41A#42E#432#446#523#56C#575
;~F#57E#587#590#620#62B#64A#680#698#6CF#6DB#6EE#705#710#73E#76B#79E#7CA#857#876#882
;~F#897#8B3#8C0#8C6#8CC#8D3#8DA#8E8#8F7#901#90A#912#91A#924#929#92E#933#938#93D#942
;~F#949#94E#955#95C#963#96A#971#976#97A
;~C#Blitz3D