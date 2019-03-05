Function LoadBuildings()
	Local dir = ReadDir("gfx/buildings")
	Repeat
		Local currentFile$ = NextFile$(dir)
		If currentFile = "" Then Exit 
		If(Right(currentFile,3) = "b3d") Then
			Local b.Building = New Building
			b\name = Left(currentFile,Len(currentFile)-4)
			b\entity = LoadMesh("gfx/buildings/"+currentFile) : HideEntity(b\entity)
			b\loadonly = True
		EndIf
	Forever 
	CloseDir(dir)
End Function

Function LoadVehicles()
	Local dir = ReadDir("gfx/vehicles")
	Repeat
		Local currentFile$ = NextFile$(dir)
		If currentFile = "" Then Exit 
		If(Right(currentFile,3) = "png") Then
			Local v.Vehicle = New Vehicle
			v\name = Left(currentFile,Len(currentFile)-4)
			v\entity = LoadTexture("gfx/vehicles/"+currentFile,4)
			v\loadonly = True
		EndIf
	Forever 
	CloseDir(dir)
End Function

Function LoadHumans()
	Local dir = ReadDir("gfx/humans")
	Repeat
		Local currentFile$ = NextFile$(dir)
		If currentFile = "" Then Exit 
		If(Right(currentFile,3) = "png") Then
			Local h.Human = New Human
			h\name = Left(currentFile,Len(currentFile)-4)
			h\entity = LoadTexture("gfx/humans/"+currentFile,4)
			h\loadonly = True
		EndIf
	Forever 
	CloseDir(dir)
End Function

Function LoadItems()
	Local dir = ReadDir("gfx/items")
	Repeat
		Local currentFile$ = NextFile$(dir)
		If currentFile = "" Then Exit 
		If(Right(currentFile,3) = "png") Then
			Local i.Item = New Item
			i\name = Left(currentFile,Len(currentFile)-4)
			i\entity = LoadTexture("gfx/items/"+currentFile,4)
			i\loadonly = True
		EndIf
	Forever 
	CloseDir(dir)
End Function

Function LoadTriggers()
	Local dir = ReadDir("gfx/triggers")
	Repeat
		Local currentFile$ = NextFile$(dir)
		If currentFile = "" Then Exit 
		If(Right(currentFile,3) = "png") Then
			Local t.Trigger = New Trigger
			t\name = Left(currentFile,Len(currentFile)-4)
			t\entity = LoadTexture("gfx/triggers/"+currentFile,4)
			t\loadonly = True
		EndIf
	Forever 
	CloseDir(dir)
End Function

Function LoadWeapons()
	Local id=1
	Local newPlayerWeapon.WeaponPossessedByHuman
	Local snd.WeaponSound
	
	For s.Var = Each Var
		If((s\name = "type") And (s\value = "shoot")) Then
			Local ws.WeaponShoot = New WeaponShoot
			ws\id = id
			ws\name = s\category
			For i.Var = Each Var
				If(i\category = ws\name) Then
					If(i\name = "shot_interval") Then
						ws\shot_interval = i\value
					ElseIf(i\name = "lifetime") Then
						ws\bulletLifeTime = i\value
					EndIf
				EndIf
			Next
			For f.Var = Each Var
				If(f\category = ws\name) Then
					If(f\name = "speed") Then
						ws\bulletSpeed = f\value
					ElseIf(f\name = "damage") Then
						ws\damage = f\value
					EndIf
				EndIf
			Next
			
			;create hud image
			Local hImage.HudWeaponImage = New HudWeaponImage
			hImage\id = id
			hImage\image = LoadImage("gfx/items/"+s\category+".png") : ScaleImage(hImage\image,0.75,0.75) : MaskImage(hImage\image,0,0,0)
			
			;add weapon to single player arsenal
			newPlayerWeapon.WeaponPossessedByHuman = New WeaponPossessedByHuman
			newPlayerWeapon\human = Handle(localPlayer)
			newPlayerWeapon\name = ws\name
			newPlayerWeapon\behaviour = "shoot"
			newPlayerWeapon\ammo = 0
			
			;sound
			snd.WeaponSound = New WeaponSound
			snd\id = id
			snd\sound = LoadSound("sfx/"+ws\name+".ogg")
			
			;...
			DebugLog("created shoot weapon '"+ws\name+"' with id "+ws\id)
			id=id+1
		ElseIf((s\name = "type") And (s\value = "throw")) Then
			Local wt.WeaponThrow = New WeaponThrow
			wt\id = id
			wt\name = s\category
			
			;create hud image
			hImage.HudWeaponImage = New HudWeaponImage
			hImage\id = id
			hImage\image = LoadImage("gfx/items/"+s\category+".png") : ScaleImage(hImage\image,0.75,0.75) : MaskImage(hImage\image,0,0,0)
			
			;add weapon to single player arsenal
			newPlayerWeapon.WeaponPossessedByHuman = New WeaponPossessedByHuman
			newPlayerWeapon\human = Handle(localPlayer)
			newPlayerWeapon\name = wt\name
			newPlayerWeapon\behaviour = "throw"
			newPlayerWeapon\ammo = 0
			
			;sound
			snd.WeaponSound = New WeaponSound
			snd\id = id
			snd\sound = LoadSound("sfx/"+wt\name+".ogg")
			
			;...
			DebugLog("created throw weapon '"+wt\name+"' with id "+wt\id)
			id=id+1
		ElseIf((s\name = "type") And (s\value = "flame")) Then
			Local wf.WeaponFlame = New WeaponFlame
			wf\id = id
			wf\name = s\category
			
			;create hud image
			hImage.HudWeaponImage = New HudWeaponImage
			hImage\id = id
			hImage\image = LoadImage("gfx/items/"+s\category+".png") : ScaleImage(hImage\image,0.75,0.75) : MaskImage(hImage\image,0,0,0)
			
			;add weapon to single player arsenal
			newPlayerWeapon.WeaponPossessedByHuman = New WeaponPossessedByHuman
			newPlayerWeapon\human = Handle(localPlayer)
			newPlayerWeapon\name = wf\name
			newPlayerWeapon\behaviour = "flame"
			newPlayerWeapon\ammo = 0
			
			;sound
			snd.WeaponSound = New WeaponSound
			snd\id = id
			snd\sound = LoadSound("sfx/"+wf\name+".ogg")
			
			;...
			DebugLog("created flame weapon '"+wt\name+"' with id "+wf\id)
			id=id+1	
		ElseIf((s\name = "type") And (s\value = "rocket")) Then
			Local wr.WeaponRocket = New WeaponRocket
			wr\id = id
			wr\name = s\category
			
			;create hud image
			hImage.HudWeaponImage = New HudWeaponImage
			hImage\id = id
			hImage\image = LoadImage("gfx/items/"+s\category+".png") : ScaleImage(hImage\image,0.75,0.75) : MaskImage(hImage\image,0,0,0)
			
			;add weapon to single player arsenal
			newPlayerWeapon.WeaponPossessedByHuman = New WeaponPossessedByHuman
			newPlayerWeapon\human = Handle(localPlayer)
			newPlayerWeapon\name = wr\name
			newPlayerWeapon\behaviour = "rocket"
			newPlayerWeapon\ammo = 0
			
			;sound
			snd.WeaponSound = New WeaponSound
			snd\id = id
			snd\sound = LoadSound("sfx/"+wr\name+".ogg")
			
			;...
			DebugLog("created rocket weapon '"+wr\name+"' with id "+wr\id)
			id=id+1	
		ElseIf((s\name = "type") And (s\value = "beat")) Then
			Local wb.WeaponBeat = New WeaponBeat
			wb\id = id
			wb\name = s\category
			
			;create hud image
			hImage.HudWeaponImage = New HudWeaponImage
			hImage\id = id
			hImage\image = LoadImage("gfx/items/"+s\category+".png") : ScaleImage(hImage\image,0.75,0.75) : MaskImage(hImage\image,0,0,0)
			
			;add weapon to single player arsenal
			newPlayerWeapon.WeaponPossessedByHuman = New WeaponPossessedByHuman
			newPlayerWeapon\human = Handle(localPlayer)
			newPlayerWeapon\name = wb\name
			newPlayerWeapon\behaviour = "beat"
			newPlayerWeapon\ammo = 0
			
			;sound
			snd.WeaponSound = New WeaponSound
			snd\id = id
			snd\sound = LoadSound("sfx/"+wb\name+".ogg")
			
			;...
			DebugLog("created beat weapon '"+wr\name+"' with id "+wb\id)
			id=id+1	
		EndIf
	Next
	
	localPlayerMaxWeaponId = id
End Function

Function LoadBehaviours()
	Local dir = ReadDir("behaviours")
	Repeat
		Local currentDirectory$ = NextFile$(dir)
		If(currentDirectory = "") Then Exit 
		If(FileType("behaviours/"+currentDirectory)=2 And currentDirectory<>"." And currentDirectory<>"..") Then
			;DebugLog("current dir: "+currentDirectory+", fileType: "+FileType("behaviours/"+currentDirectory))
			;read behaviours from this directory
			Local innerDir = ReadDir("behaviours/"+currentDirectory)
			Repeat
				Local currentFile$ = NextFile(innerDir)
				If(currentFile = "") Then Exit
				If(Right(currentFile,3) = "bhv") Then
					Local b.Behaviour = New Behaviour
					b\name = Left(currentFile,Len(currentFile)-4)
					b\category = currentDirectory
					
					;load behaviour script text
					fileStream = ReadFile("behaviours/"+currentDirectory+"/"+currentFile)
					Local lineNr = 1
					While(Not(Eof(fileStream)))
						Local txt$ = ReadLine(fileStream)
						;trim line (no spaces, tabs before the first character)
						Local trimOffset=1
						Repeat
							Local currentChar$ = Mid(txt,trimOffset,1)
							If((Asc(currentChar) >= 33) Or (currentChar = "")) Then Exit
							trimOffset=trimOffset+1
						Forever
						txt = Mid(txt,trimOffset)
						If((txt <> "") And (Left(txt,2) <> "//")) Then
							Local sl.ScriptLine = New ScriptLine
							sl\behaviour = Left(currentFile,Len(currentFile)-4)
							sl\category = currentDirectory
							sl\lineNr = lineNr
							sl\txt = txt
							DebugLog("script line nr "+sl\lineNr+": "+sl\txt)
							lineNr=lineNr+1
						EndIf
					Wend
					CloseFile(fileStream)
					
					;DebugLog("inner dir="+currentDirectory+", file="+currentFile)
				EndIf
			Forever
		EndIf
	Forever 
	CloseDir(dir)
End Function

Function LoadHud()
	hudLife = LoadAnimImage("gfx/life.png",32,32,0,20)
	MaskImage(hudLife,0,0,0)
	
	hudCash = LoadImage("gfx/cash.png")
	MaskImage(hudCash,0,0,0)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D