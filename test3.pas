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
  Number := 0;  
  WriteLn('Constructor called: Number initialized to 0');
end;

procedure TCalculator.ShowNumber;
begin
  WriteLn('Your entered number is: ', Number);
end;

var
  Calc: TCalculator;
begin  
  Calc := TCalculator.Create;   
  Calc.ShowNumber;
end.
