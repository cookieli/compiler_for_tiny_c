.file "tests/codeGenOfMe/bool_assign.dcf"
.text
.section .rodata
.LC0:
.string "%d \n"
.LC1:
.string "%d\n"
.LC2:
.string "%d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movb $1,-4(%rbp)
movb -4(%rbp),%al
cmpb $0,%al
jle .L1
movb $0,-3(%rbp)
jmp .L2
.L1:
movb $1,-3(%rbp)
.L2:
movb -4(%rbp),%al
cmpb $0,%al
jle .L3
.L4:
movb $1,-1(%rbp)
jmp .L5
.L3:
movb -3(%rbp),%al
cmpb $0,%al
jle .L6
jmp .L4
.L6:
movb $0,-1(%rbp)
.L5:
movb -4(%rbp),%al
cmpb $0,%al
jle .L7
movb -3(%rbp),%al
cmpb $0,%al
jle .L7
movb $1,-2(%rbp)
jmp .L8
.L7:
movb $0,-2(%rbp)
.L8:
movsbq -3(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movsbq -1(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movsbq -2(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

