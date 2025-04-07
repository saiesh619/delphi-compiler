program LocalVar;

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
end;  

procedure TCalculator.ShowNumber;
begin 
    var j:Integer;
    j:= 115;     
      WriteLn('Local variable ', j);      
end;

var
  Calc: TCalculator;
begin
  var j : Integer;
  j:= 15 ;
  Calc := TCalculator.Create;   
  Calc.ShowNumber;
end.
