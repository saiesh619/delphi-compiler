
@.str7 = constant [11 x i8] c"Counter = \00"
; ModuleID = 'main'
declare i32 @printf(i8*, ...)
declare i32 @read_int()
@.str = constant [4 x i8] c"%d\0A\00"

define i32 @main() {
 %t0 = alloca i32
 store i32 1, i32* %t0
 br label %label0
 label0:
 %t1 = load i32, i32* %t0
 %t2 = icmp sle i32 %t1, 10
 br i1 %t2, label %label1, label %label3
 label1:
 %t3 = load i32, i32* %t0
 %t4 = icmp slt i32 %t3, 5
 br i1 %t4, label %label4, label %label5
 label4:
 br label %label2
 br label %label5
 label5:
 %t5 = load i32, i32* %t0
 %t6 = icmp sgt i32 %t5, 7
 br i1 %t6, label %label6, label %label7
 label6:
 br label %label3
 br label %label7
 label7:
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([11 x i8], [11 x i8]* @.str7, i32 0, i32 0))
 %t8 = load i32, i32* %t0
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 %t8)
 br label %label2
 label2:
 %t9 = load i32, i32* %t0
 %t10 = add i32 %t9, 1
 store i32 %t10, i32* %t0
 br label %label0
 label3:
 ret i32 0
}
