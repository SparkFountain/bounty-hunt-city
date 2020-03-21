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