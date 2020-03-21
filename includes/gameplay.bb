Global player.T_Player
Const FRAME_DELAY = 30

Type T_Player
    Field entity, texture, textureFrame, nextFrameTime
    Field x#, y#, z#
    Field pitch#, yaw#, roll#
    Field energy#, maxEnergy#
    Field weapon[8]
End Type

Function InitPlayer()
    player = New T_Player
    player\entity = CreateCube()
    ; RotateEntity player\entity, 0, 180, 0
    player\texture = LoadAnimTexture("gfx/humans/player/walk.png", 4, 64, 64, 0, 15)
    EntityTexture player\entity, player\texture
    player\textureFrame = 14
    player\nextFrameTime = ms + 100

    player\x = 0 : player\z = 0 : player\y = TerrainY(map, player\x, 0, player\z)
    player\pitch = 0 : player\yaw = 0 : player\roll = 0

    player\energy = 100 : player\maxEnergy = 100

    PositionEntity player\entity, player\x, player\y, player\z
    ; center camera to player
    PositionEntity cam, player\x, player\y + 20, player\z
End Function

Function PlayerControls()
    ; MOTION
    Local turning = False
    local walking = False

    If KeyDown(KEY_ARROW_UP) Then
        MoveEntity player\entity, 0, 0, 0.1
        walking = True
    EndIf
    If KeyDown(KEY_ARROW_DOWN) Then
        MoveEntity player\entity, 0, 0, -0.1
        walking = True
    EndIf
    If KeyDown(KEY_ARROW_LEFT) Then
        TurnEntity player\entity, 0, 3, 0
        turning = True
    EndIf
    If KeyDown(KEY_ARROW_RIGHT) Then
        TurnEntity player\entity, 0, -3, 0
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

    ; FART
    If KeyHit(KEY_TAB) And (Not ChannelPlaying(channelFart)) Then channelFart = PlaySound(soundFart(Rand(0,3)))
End Function