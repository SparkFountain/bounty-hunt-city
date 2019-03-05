Function LoadMap(name$, inEditor=True)
	;does the map exist?
	If(FileType("maps/"+name+".map")<>1) Then
		GUI_MsgBox("No such map","The map "+name+" does not exist.")
		Return
	EndIf
	
	;delete old stuff
	DeleteAllOnMap(inEditor)
	
	fileStream = ReadFile("maps/"+name+".map")
	
	;header
	ReadLine(fileStream) : ReadLine(fileStream) : ReadLine(fileStream)
	
	;buildings
	ReadLine(fileStream)
	Local numOfBuildings = ReadInt(fileStream)
	Local maxBuildingId = 0
	For i=1 To numOfBuildings
		Local newB.Building = New Building
		newB\id = ReadInt(fileStream)
		If(newB\id > maxBuildingId) Then maxBuildingId = newB\id : buildingId = maxBuildingId
		newB\name = ReadString(fileStream)
		For findB.Building = Each Building
			If(findB\name = newB\name And findb\loadonly) Then
				newB\entity = CopyEntity(findB\entity)
				PositionEntity(newB\entity,ReadFloat(fileStream),ReadFloat(fileStream),ReadFloat(fileStream))
				RotateEntity(newB\entity,0,ReadFloat(fileStream),0)
				EntityPickMode(newB\entity,2)
				Exit
			EndIf
		Next
	Next
	
	;vehicles
	ReadLine(fileStream)
	Local numOfVehicles = ReadInt(fileStream)
	Local maxVehicleId = 0
	For i=1 To numOfVehicles
		Local newV.Vehicle = New Vehicle
		newV\id = ReadInt(fileStream)
		If(newV\id > maxVehicleId) Then maxVehicleId = newV\id : vehicleId = maxVehicleId
		newV\name = ReadString(fileStream)
		For findV.Vehicle = Each Vehicle
			If(findV\name = newV\name And findV\loadonly) Then
				newV\category = GetString(newV\name,"category")
				If(inEditor) Then
					newV\entity = CreateCube() : ScaleEntity(newV\entity,GetFloat(newV\name,"scale_x"),FlatEntityY,GetFloat(newV\name,"scale_z"))
					PositionEntity(newV\entity,ReadFloat(fileStream),ReadFloat(fileStream),ReadFloat(fileStream))
					RotateEntity(newV\entity,0,ReadFloat(fileStream),0)
					EntityTexture(newV\entity,findV\entity)
					EntityPickMode(newV\entity,2)
				Else
					newV\entity = CreateCube() : ScaleEntity(newV\entity,GetFloat(newV\name,"scale_x"),FlatEntityY,GetFloat(newV\name,"scale_z"))
					EntityTexture(newV\entity,findV\entity)
					Local z#=ReadFloat(fileStream)
					Local y#=ReadFloat(fileStream) ;: DebugLog("y: "+y)
					Local x#=ReadFloat(fileStream)
					PositionEntity(newV\entity,x,y,z)
					;lights
					CreateVehicleLights(newV\name, newV\entity)
					RotateEntity(newV\entity,0,ReadFloat(fileStream),0)
					newV\damageEmitter = CreatePivot(newV\entity)	;probably wrong?
					
					
				EndIf
				newV\energy = ReadInt(fileStream)
				newV\maxEnergy = ReadInt(fileStream)
				Exit
			EndIf
		Next
	Next
	
	;humans
	ReadLine(fileStream)
	Local numOfHumans = ReadInt(fileStream)
	Local maxHumanId
	For i=1 To numOfHumans
		Local newH.Human = New Human
		newH\id = ReadInt(fileStream)
		If(newH\id > maxHumanId) Then maxHumanId = newH\id : humanId = maxHumanId
		newH\name = ReadString(fileStream)
		For findH.Human = Each Human
			If(findH\name = newH\name And findH\loadonly) Then
				If(inEditor) Then
					newH\entity = CreateCube() : ScaleEntity(newH\entity,1,FlatEntityY,1)
					PositionEntity(newH\entity,ReadFloat(fileStream),ReadFloat(fileStream),ReadFloat(fileStream))
					RotateEntity(newH\entity,0,ReadFloat(fileStream),0)
					EntityTexture(newH\entity,findH\entity)
					EntityPickMode(newH\entity,2)
				Else
					newH\entity = CreateCube() : ScaleEntity(newH\entity,1,FlatEntityY,1)
					PositionEntity(newH\entity,ReadFloat(fileStream),ReadFloat(fileStream),ReadFloat(fileStream))
					;PositionEntity(newH\entity,EntityX(newH\entity),HUMAN_Y,EntityZ(newH\entity))	;debug!
					RotateEntity(newH\entity,0,ReadFloat(fileStream),0)
					EntityTexture(newH\entity,findH\entity)
					newH\colSphere = CreateSphere(2) : ScaleEntity(newH\colSphere,0.5,0.5,0.5) : EntityAlpha(newH\colSphere,0)
					PositionEntity(newH\colSphere,EntityX(newH\entity),EntityY(newH\entity),EntityZ(newH\entity))
					EntityParent(newH\colSphere,newH\entity)
				EndIf
				newH\energy = ReadInt(fileStream)
				newH\maxEnergy = ReadInt(fileStream)
				newH\behaviour = ReadString(fileStream)
				If(Not(inEditor)) Then
					Select newH\behaviour
					;single player?
						Case "singleplayer"
							localPlayer.Human = newH
							localPlayer\texture = LoadAnimTexture("gfx/humans/"+newH\name+"/walk.png",4,64,64,0,15)
					;local network?
						Case "local network player 1"
							If(localPlayerNumber = 1) Then
								localPlayer.Human = newH
								localPlayer\texture = LoadAnimTexture("gfx/humans/"+newH\name+"/walk.png",4,64,64,0,15)
							EndIf
						Case "local network player 2"
							If(localPlayerNumber = 2) Then
								localPlayer.Human = newH
								localPlayer\texture = LoadAnimTexture("gfx/humans/"+newH\name+"/walk.png",4,64,64,0,15)
							EndIf
						Case "local network player 3"
							If(localPlayerNumber = 3) Then
								localPlayer.Human = newH
								localPlayer\texture = LoadAnimTexture("gfx/humans/"+newH\name+"/walk.png",4,64,64,0,15)
							EndIf
						Case "local network player 4"
							If(localPlayerNumber = 4) Then
								localPlayer.Human = newH
								localPlayer\texture = LoadAnimTexture("gfx/humans/"+newH\name+"/walk.png",4,64,64,0,15)
							EndIf
						Case "local network player 5"
							If(localPlayerNumber = 5) Then
								localPlayer.Human = newH
								localPlayer\texture = LoadAnimTexture("gfx/humans/"+newH\name+"/walk.png",4,64,64,0,15)
							EndIf
						Case "local network player 6"
							If(localPlayerNumber = 6) Then
								localPlayer.Human = newH
								localPlayer\texture = LoadAnimTexture("gfx/humans/"+newH\name+"/walk.png",4,64,64,0,15)
							EndIf
					End Select
				EndIf
				Exit
			EndIf
		Next
	Next
	
	;items
	ReadLine(fileStream)
	Local numOfItems = ReadInt(fileStream)
	Local maxItemId = 0
	For i=1 To numOfItems
		Local newI.Item = New Item
		newI\id = ReadInt(fileStream)
		If(newI\id > maxItemId) Then maxItemId = newI\id : itemId = maxItemId
		newI\name = ReadString(fileStream)
		For findI.Item = Each Item
			If(findI\name = newI\name And findI\loadonly) Then
				newI\entity = CreateCube() : ScaleEntity(newI\entity,1,FlatEntityY,1)
				PositionEntity(newI\entity,ReadFloat(fileStream),ReadFloat(fileStream),ReadFloat(fileStream))
				EntityTexture(newI\entity,findI\entity)
				EntityFX(newI\entity,1)
				EntityPickMode(newI\entity,2)
				newI\value = ReadInt(fileStream)
				Exit
			EndIf
		Next
	Next
	
	;triggers
	ReadLine(fileStream)
	Local numOfTriggers = ReadInt(fileStream)
	Local maxTriggerId
	For i=1 To numOfTriggers
		If(inEditor) Then
			Local newT.Trigger = New Trigger
			newT\id = ReadInt(fileStream)
			If(newT\id > maxTriggerId) Then maxTriggerId = newT\id : triggerId = maxTriggerId
			newT\name = ReadString(fileStream)
			For findT.Trigger = Each Trigger
				If(findT\name = newT\name And findT\loadonly) Then
					newT\entity = CreateCube() : ScaleEntity(newT\entity,1,FlatEntityY,1)
					PositionEntity(newT\entity,ReadFloat(fileStream),ReadFloat(fileStream),ReadFloat(fileStream))
					EntityTexture(newT\entity,findT\entity)
					EntityPickMode(newT\entity,2)
					Exit
				EndIf
			Next
		Else
;			Local triggerName$ = ReadString(fileStream)
;			Select triggerName
;				Case "player start position"
;					singlePlayer.Human = New Human
;					singlePlayer\name = "single player"
;					singlePlayer\colSphere = CreateSphere() : EntityAlpha(singlePlayer\colSphere,0)
;					singlePlayer\sprite = LoadSprite("gfx/humans/player.png",4,singlePlayer\colSphere) ; change
;					PositionEntity(singlePlayer\colSphere,ReadFloat(fileStream),ReadFloat(fileStream),ReadFloat(fileStream))
;					singlePlayer\energy = singlePlayer\maxEnergy = 100 ; change
;			End Select
		EndIf
	Next
	
	CloseFile(fileStream)
End Function

Function SaveMap(name$)
	fileStream = WriteFile("maps/"+name+".map")
	WriteLine(fileStream,"This is a BOUNTY HUNT CITY map.")
	WriteLine(fileStream,"Game © by Spark Fountain")
	WriteLine(fileStream,"===============================")
	
	;buildings
	WriteLine(fileStream,"[buildings]")
	Local numOfBuildings
	For b.Building = Each Building
		If(Not(b\loadonly)) Then numOfBuildings=numOfBuildings+1
	Next
	WriteInt(fileStream,numOfBuildings)
	For b.Building = Each Building
		If(Not(b\loadonly)) Then
			WriteInt(fileStream,b\id)
			WriteString(fileStream,b\name) : WriteFloat(fileStream,EntityZ(b\entity)) : WriteFloat(fileStream,EntityY(b\entity)) : WriteFloat(fileStream,EntityX(b\entity))
			WriteFloat(fileStream,EntityYaw(b\entity))
		EndIf
	Next
	
	;vehicles
	WriteLine(fileStream,"[vehicles]")
	Local numOfVehicles
	For v.Vehicle = Each Vehicle
		If(Not(v\loadonly)) Then numOfVehicles=numOfVehicles+1
	Next
	WriteInt(fileStream,numOfVehicles)
	For v.Vehicle = Each Vehicle
		If(Not(v\loadonly)) Then
			WriteInt(fileStream,v\id)
			WriteString(fileStream,v\name) : WriteFloat(fileStream,EntityZ(v\entity)) : WriteFloat(fileStream,EntityY(v\entity)) : WriteFloat(fileStream,EntityX(v\entity))
			WriteFloat(fileStream,EntityYaw(v\entity))
			WriteInt(fileStream,v\energy) : WriteInt(fileStream,v\maxEnergy)
		EndIf
	Next
	
	;humans
	WriteLine(fileStream,"[humans]")
	Local numOfHumans
	For h.Human = Each Human
		If(Not(h\loadonly)) Then numOfHumans=numOfHumans+1
	Next
	WriteInt(fileStream,numOfHumans)
	For h.Human = Each Human
		If(Not(h\loadonly)) Then
			WriteInt(fileStream,h\id)
			WriteString(fileStream,h\name) : WriteFloat(fileStream,EntityZ(h\entity)) : WriteFloat(fileStream,EntityY(h\entity)) : WriteFloat(fileStream,EntityX(h\entity))
			WriteFloat(fileStream,EntityYaw(h\entity))
			WriteInt(fileStream,h\energy) : WriteInt(fileStream,h\maxEnergy) : WriteString(fileStream,h\behaviour)
		EndIf
	Next
	
	;items
	WriteLine(fileStream,"[items]")
	Local numOfItems
	For i.Item = Each Item
		If(Not(i\loadonly)) Then numOfItems=numOfItems+1
	Next
	WriteInt(fileStream,numOfItems)
	For i.Item = Each Item
		If(Not(i\loadonly)) Then
			WriteInt(fileStream,i\id)
			WriteString(fileStream,i\name) : WriteFloat(fileStream,EntityZ(i\entity)) : WriteFloat(fileStream,EntityY(i\entity)) : WriteFloat(fileStream,EntityX(i\entity))
			WriteInt(fileStream,i\value)
		EndIf
	Next
	
	;triggers
	WriteLine(fileStream,"[triggers]")
	Local numOfTriggers
	For t.Trigger = Each Trigger
		If(Not(t\loadonly)) Then numOfTriggers=numOfTriggers+1
	Next
	WriteInt(fileStream,numOfTriggers)
	For t.Trigger = Each Trigger
		If(Not(t\loadonly)) Then
			WriteInt(fileStream,t\id)
			WriteString(fileStream,t\name) : WriteFloat(fileStream,EntityZ(t\entity)) : WriteFloat(fileStream,EntityY(t\entity)) : WriteFloat(fileStream,EntityX(t\entity))
			;value?
		EndIf
	Next
	
	CloseFile(fileStream)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D