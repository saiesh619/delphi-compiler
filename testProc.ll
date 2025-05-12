@.str5 = constant [11 x i8] c"Counter = \00"
define void @SayHellos() {
 %t0 = alloca i32
 store i32 0, i32* %t0
 store i32 0, i32* %t0
 br label %label0
 label0:
 %t1 = load i32, i32* %t0
 %t2 = icmp slt i32 %t1, 10
 br i1 %t2, label %label1, label %label2
 label1:
 %t3 = load i32, i32* %t0
 %t4 = add i32 %t3, 1
 store i32 %t4, i32* %t0
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([11 x i8], [11 x i8]* @.str5, i32 0, i32 0))
 %t6 = load i32, i32* %t0
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 %t6)
 br label %label0
 label2:
 ret void
}


@.str8 = constant [16 x i8] c"Program ended.\0A\00"
@.str7 = constant [18 x i8] c"Program started.\0A\00"
; ModuleID = 'main'
declare i32 @printf(i8*, ...)
declare i32 @read_int()
@.str = constant [4 x i8] c"%d\0A\00"

define i32 @main() {
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([18 x i8], [18 x i8]* @.str7, i32 0, i32 0))
 call i32 @SayHellos()
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([16 x i8], [16 x i8]* @.str8, i32 0, i32 0))
 ret i32 0
}
