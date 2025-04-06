program NumberIO;

type
  TCalculator = class
  private  
    Number: Integer;
  public   
    constructor Create;    
    procedure ShowNumber;
  end;


constructor TCalculator.Create;
begin
  WriteLn('Constructor called: Number initialized to 0');     
  WriteLn('Constructor called: Number initialized to 0');
end;  

procedure TCalculator.ShowNumber;
begin
  var i:Integer;
  var j: Integer;
  j:= 5; 
  Number:= 2;
  WriteLn('global variable ', global);
  for i := 1 to 10 do
  begin
      WriteLn('Local variable ', j);
      WriteLn('Class variable ', Number);
  end;
end;

var
  Calc: TCalculator;
begin
  var global : Integer;
  global:= 15 ;
  Calc := TCalculator.Create;   
  Calc.ShowNumber;
end.
