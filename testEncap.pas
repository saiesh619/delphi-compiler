program TestSelfSupport;

type
  TCounter = class
  private
    Value: Integer;
  public
    constructor Create;
    procedure SetValue;
    procedure Increment;
    function GetValue;
  end;

procedure TCounter.SetValue;
begin
  Value := 0;
end;

procedure TCounter.Increment;
begin
  Value := Value + 1;
end;

function TCounter.GetValue;
begin
  result := Value;
end;

var
  Counter: TCounter;
begin
  Counter := TCounter.Create;
  Counter.SetValue;
  Counter.Increment;
  Counter.Increment;
  Counter.Increment;
  Writeln('Counter = ', Counter.GetValue());
end.
