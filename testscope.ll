
@.str15 = constant [18 x i8] c"After WHILE: x = \00"
@.str13 = constant [25 x i8] c"After IF, in WHILE: x = \00"
@.str11 = constant [25 x i8] c"Inside IF (x < 60): x = \00"
@.str6 = constant [19 x i8] c"Inside WHILE: x = \00"
@.str1 = constant [12 x i8] c"Start: x = \00"
; ModuleID = 'main'
declare i32 @printf(i8*, ...)
declare i32 @read_int()
@.str = constant [4 x i8] c"%d\0A\00"

define i32 @main() {
 %t0 = alloca i32
 store i32 0, i32* %t0
 store i32 100, i32* %t0
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([12 x i8], [12 x i8]* @.str1, i32 0, i32 0))
 %t2 = load i32, i32* %t0
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 %t2)
 br label %label0
 label0:
 %t3 = load i32, i32* %t0
 %t4 = icmp sgt i32 %t3, 90
 br i1 %t4, label %label1, label %label2
 label1:
 %t5 = alloca i32
 store i32 0, i32* %t5
 store i32 50, i32* %t5
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([19 x i8], [19 x i8]* @.str6, i32 0, i32 0))
 %t7 = load i32, i32* %t5
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 %t7)
 %t8 = load i32, i32* %t5
 %t9 = icmp slt i32 %t8, 60
 br i1 %t9, label %label3, label %label4
 label3:
 %t10 = alloca i32
 store i32 0, i32* %t10
 store i32 25, i32* %t10
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([25 x i8], [25 x i8]* @.str11, i32 0, i32 0))
 %t12 = load i32, i32* %t10
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 %t12)
 br label %label4
 label4:
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([25 x i8], [25 x i8]* @.str13, i32 0, i32 0))
 %t14 = load i32, i32* %t5
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 %t14)
 br label %label2
 br label %label0
 label2:
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([18 x i8], [18 x i8]* @.str15, i32 0, i32 0))
 %t16 = load i32, i32* %t0
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 %t16)
 ret i32 0
}
