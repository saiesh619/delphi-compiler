program NumberIO;

type
  TCalculator = class
  private  
    Number: Integer;
  public   
    constructor Create;
    procedure ReadNumber;
    procedure ShowNumber;
    destructor Destroy;
  end;

procedure TCalculator.ReadNumber;
begin
  Write('Enter a number : ');
  ReadLn(Number);
end;

procedure TCalculator.ShowNumber;
begin
  WriteLn('Your number is : ', Number);
end;

var
  Calc: TCalculator;
  Number: Integer;
begin  
  Calc := TCalculator.Create;
  Calc.ReadNumber;
  Calc.ShowNumber;
end.
