.lib " "

GUI_InitGUI%(skin_path$)
GUI_FreeGUI%()
GUI_LoadSkin%(path$)
GUI_UpdateGUI%(pointer_frame%)
GUI_CreateWindow%(x%,y%,w%,h%,txt$,icon$,btn_close%,btn_max%,btn_min%,btn_hlp%)
GUI_CreateTab%(owner%,x%,y%,w%,h%)
GUI_CreateTabPage%(owner%,txt$,icon$,enabled%)
GUI_CreateGroupBox%(owner%,x%,y%,w%,h%,txt$)
GUI_CreateButton%(owner%,x%,y%,w%,h%,txt$,icon$,enabled%)
GUI_CreateCheckBox%(owner%,x%,y%,txt$,enabled%,checked%)
GUI_CreateRadio%(owner%,x%,y%,txt$,gid%,enabled%,checked%)
GUI_CreateImage%(owner%,x%,y%,w%,h%,img$)
GUI_CreateSlider%(owner%,x%,y%,w%,value#,min#,max#,enabled%)
GUI_CreateSpinner%(owner%,x%,y%,w%,value#,min#,max#,inc#,enabled%)
GUI_CreateLabel%(owner%,x%,y%,txt$,align$)
GUI_CreateComboBox%(owner%,x%,y%,w%,enabled%)
GUI_CreateProgressBar%(owner%,x%,y%,w%,h%,status%)
GUI_CreateScrollBar%(owner%,x%,y%,h%,status%)
GUI_CreateIcon%(owner%,x%,y%,w%,h%,txt$,icon$,enabled%,gid%)
GUI_CreateListBox%(owner%,x%,y%,w%,h%)
GUI_CreateEdit%(owner%,x%,y%,w%,txt$)
GUI_CreateMenu%(owner%,txt$)
GUI_Message$(gad%,msg$,p1$,p2$,p3$)
GUI_AppEvent%()
GUI_MsgBox%(title$,msg$,buttons%)
GUI_ColorPicker$()