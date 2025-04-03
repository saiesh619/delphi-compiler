program PersonIO;

type
  Person = class
  private  
    Name: String;
  public   
    constructor Create;
    procedure takeName;
    procedure showName;
    destructor Destroy;
  end;

procedure Person.takeName;
begin
  Write('What is your name ?');
  ReadLn(Name);
end;

procedure Person.showName;
begin
  WriteLn('NICE, Hello ', Name);
end;

var
  Per: Person;
  Name: String;
begin  
  Per := Person.Create;
  Per.takeName;
  Per.showName;
end.
