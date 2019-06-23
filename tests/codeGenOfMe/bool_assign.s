.file "tests/codeGenOfMe/bool_assign.dcf"
.text
.section .rodata
.LC0:
.string "%d \n"
.LC1:
.string "%d\n"
.LC2:
.string "%d\n"
.LC3:
.string "%d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $3,-16(%rbp)
movq $6,-8(%rbp)
movb $1,-21(%rbp)
movb -21(%rbp),%al
cmpb $0,%al
jle .L1
movb $0,-20(%rbp)
jmp .L2
.L1:
movb $1,-20(%rbp)
.L2:
movb -21(%rbp),%al
cmpb $0,%al
jle .L3
.L4:
movb $1,-18(%rbp)
jmp .L5
.L3:
movb -20(%rbp),%al
cmpb $0,%al
jle .L6
jmp .L4
.L6:
movb $0,-18(%rbp)
.L5:
movb -21(%rbp),%al
cmpb $0,%al
jle .L7
movb -20(%rbp),%al
cmpb $0,%al
jle .L7
movb $1,-19(%rbp)
jmp .L8
.L7:
movb $0,-19(%rbp)
.L8:
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jle .L9
movb $1,-17(%rbp)
jmp .L10
.L9:
movb $0,-17(%rbp)
.L10:
movsbq -20(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movsbq -18(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movsbq -19(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movsbq -17(%rbp),%rsi
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

