;THANK YOU BLITZFORUM HELP FOR THIS FUNCTION! ;)
Function Int_IP(IP$) 
	a1 = Int(Left(IP$, Instr(IP$, ".") - 1)) : IP$ = Right(IP$, Len(IP$) - Instr(IP$, ".")) 
	a2 = Int(Left(IP$, Instr(IP$, ".") - 1)) : IP$ = Right(IP$, Len(IP$) - Instr(IP$, ".")) 
	a3 = Int(Left(IP$, Instr(IP$, ".") - 1)) : IP$ = Right(IP$, Len(IP$) - Instr(IP$, ".")) 
	a4 = Int(IP$) 
	Return (a1 Shl 24) + (a2 Shl 16) + (a3 Shl 8 ) + a4 
End Function 
;~IDEal Editor Parameters:
;~C#Blitz3D