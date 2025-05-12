program TestReturn;

function Add;
begin
  var x: integer;
  x := 8;
  result := x; 
end;

begin
  var result: integer;
  writeln('Result = ', Add());
end.
