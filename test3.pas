program forLoop;

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
  for i := 1 to 10 do
  begin
      WriteLn('Local variable ',i);      
  end;
end;

var
  Calc: TCalculator;
begin
  Calc := TCalculator.Create;   
  Calc.ShowNumber;
end.
