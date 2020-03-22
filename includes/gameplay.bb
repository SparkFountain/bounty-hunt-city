Global player.T_Player
Const FRAME_DELAY = 30

Type T_Player
    Field entity, texture, textureFrame, nextFrameTime
    Field x#, y#, z#
    Field pitch#, yaw#, roll#
    Field energy#, maxEnergy#
    Field weapon$[9], activeWeapon
    Field cash
    Field listener
End Type

Function InitPlayer()
    Local entity = CreateMesh() 
	Local surf = CreateSurface(entity) 
	
	Local v0 = AddVertex(surf, -1, 1, -1, 0, 1) 
	Local v1 = AddVertex(surf,  1, 1, -1, 1, 1) 
	Local v2 = AddVertex(surf,  1, 1,  1, 1, 0)
	Local v3 = AddVertex(surf, -1, 1,  1, 0, 0)
	
	Local tri1 = AddTriangle(surf,v0,v2,v1)
	Local tri2 = AddTriangle(surf,v0,v3,v2)

    player = New T_Player
    player\entity = entity
    player\texture = LoadAnimTexture("gfx/humans/player/walk.png", 4, 64, 64, 0, 15)
    EntityTexture player\entity, player\texture
    player\textureFrame = 14
    player\nextFrameTime = ms + FRAME_DELAY

    ; Position and Orientation
    player\x = 0 : player\z = 0 : player\y = TerrainY(map, player\x, 0, player\z)
    player\pitch = 0 : player\yaw = 0 : player\roll = 0
    PositionEntity player\entity, player\x, player\y, player\z
    ; center camera to player
    PositionEntity cam, player\x, player\y + 20, player\z

    ; Collision Handling
    EntityRadius player\entity, 0.5
    EntityType player\entity, COLLISION_PLAYER
    Collisions COLLISION_PLAYER, COLLISION_VEHICLE, COLLISION_METHOD_SPHERE_BOX, COLLISION_REACTION_STOP
    Collisions COLLISION_PLAYER, COLLISION_HUMAN, COLLISION_METHOD_SPHERE_BOX, COLLISION_REACTION_SLIDE  ; TODO: slide or stop?

    ; Energy
    player\energy = 100 : player\maxEnergy = 100

    ; Cash
    player\cash = 135289 ; TODO: set to 0 later

    ; Weapons
    player\weapon[1] = "Pistol"

    ; 3D Sound Listener
    player\listener = CreatePivot()
    CreateListener(player\listener, 0.4, 1, 10)
End Function

Function PlayerControls()
    ; MOTION
    Local turning = False
    local walking = False

    If KeyDown(KEY_ARROW_UP) Or KeyDown(KEY_W) Then
        MoveEntity player\entity, 0, 0, 0.1
        walking = True
    EndIf
    If KeyDown(KEY_ARROW_DOWN) Or KeyDown(KEY_S) Then
        MoveEntity player\entity, 0, 0, -0.1
        walking = True
    EndIf
    If KeyDown(KEY_ARROW_LEFT) Or KeyDown(KEY_A) Then
        TurnEntity player\entity, 0, 3.5, 0
        turning = True
    EndIf
    If KeyDown(KEY_ARROW_RIGHT) Or KeyDown(KEY_D) Then
        TurnEntity player\entity, 0, -3.5, 0
        turning = True
    EndIf

    If turning Or walking then
        ; calculate player position and angles
        player\x = EntityX(player\entity)
        player\z = EntityZ(player\entity)
        player\y = TerrainY(map, player\x, 0, player\z) + 1
        player\pitch = EntityPitch(player\entity)
        player\yaw = EntityYaw(player\entity)
        player\roll = EntityRoll(player\entity)

        ; apply vertical height (y) to player
        PositionEntity player\entity, player\x, player\y, player\z

        ; update listener position
        PositionEntity player\listener, player\x, player\y, player\z
    EndIf

    If walking Then
        ; animate player
        If player\nextFrameTime < ms Then
            player\textureFrame = (player\textureFrame + 1) Mod 15
            player\nextFrameTime = ms + FRAME_DELAY
            EntityTexture player\entity, player\texture, player\textureFrame
        EndIf

        ; center camera to player
        PositionEntity cam, player\x, player\y + 20, player\z
    Else
        player\textureFrame = 14
        EntityTexture player\entity, player\texture, player\textureFrame
    EndIf

    ; HANDLE WEAPON
    If KeyDown(KEY_CTRL_LEFT) Then
        Select player\activeWeapon
            Case 0 ; Melee
            
            Case 1 ; Pistol
                Local pistol.T_Pistol
                For pistol.T_Pistol = Each T_Pistol
                    If player\weapon[1] = pistol\name Then
                        If ms > lastWeaponTrigger(1) + pistol\shotInterval Then
                            lastWeaponTrigger(1) = ms ; update trigger time
                            PlaySound pistol\shotSound
                            Local bullet.T_Bullet = New T_Bullet
                            bullet\entity = CopyEntity(pistol\bulletEntity)
                            bullet\speed = pistol\bulletSpeed
                            bullet\lifeTime = ms + pistol\bulletLifeTime
                            bullet\damage = pistol\damage
                            PositionEntity bullet\entity, player\x, player\y, player\z
                            RotateEntity bullet\entity, player\pitch, player\yaw, player\roll
                            MoveEntity bullet\entity, 0, 0, 1
                            ShowEntity bullet\entity
                        EndIf
                    EndIf
                Next
            Case 2 ; Machine Pistol
            
            Case 3 ; Machine Gun
           
            Case 4 ; Rifle
           
            Case 5 ; Grenade
           
            Case 6 ; Molotov Cocktail
           
            Case 7 ; Flame Thrower
           
            Case 8 ; Electro Weapon
           
            Case 9 ; Rocket Launcher
        End Select
    EndIf

    ; CHANGE WEAPON
    If KeyHit(KEY_Y) Then
        If player\activeWeapon = 0 Then
            player\activeWeapon = 9
        Else
            player\activeWeapon = player\activeWeapon - 1
        EndIf
    ElseIf KeyHit(KEY_X) Then
        player\activeWeapon = (player\activeWeapon + 1) Mod 10
    EndIf

    ; FART
    If KeyHit(KEY_TAB) And (Not ChannelPlaying(channelFart)) Then channelFart = PlaySound(soundFart(Rand(0,3)))
End Function
