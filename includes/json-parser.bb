Type T_Property
	Field category$
	Field key$
	Field value$
End Type

Const JSON_COMMA = ","
Const JSON_COLON = ":"
Const JSON_LEFTBRACKET = "["
Const JSON_RIGHTBRACKET = "]"
Const JSON_LEFTBRACE = "{"
Const JSON_RIGHTBRACE = "}"
Const JSON_QUOTE = Chr(34)

Const JSON_QUOTE = ""
Const JSON_WHITESPACE = [" ", "\t", "\b", "\n", "\r"]
Const JSON_SYNTAX = [JSON_COMMA, JSON_COLON, JSON_LEFTBRACKET, JSON_RIGHTBRACKET, JSON_LEFTBRACE, JSON_RIGHTBRACE]

Const FALSE_LEN = Len("False")
Const TRUE_LEN = Len("True")
Const NULL_LEN = Len("Null")









Function ParseJSON(file$, category$)
	
	
	Local keyFile = ReadFile(file)
	While Not Eof(keyFile)
		Local char = Chr(ReadByte(keyFile))
		
		
		Print 
	Wend
	CloseFile keyFile
End Function