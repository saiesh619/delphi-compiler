program TestScope;
begin
  var x: integer;
  x := 100;
  writeln('Start: x = ', x);

  while x > 90 do
  begin
    var x: integer;
    x := 50;
    writeln('Inside WHILE: x = ', x);

    if x < 60 then
    begin
      var x: integer;
      x := 25;
      writeln('Inside IF (x < 60): x = ', x);
    end;

    writeln('After IF, in WHILE: x = ', x);

    break;
  end;

  writeln('After WHILE: x = ', x);
end.
