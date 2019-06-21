.file "tests/codeGenOfMe/while_02.dcf"
.text
.section .rodata
.LC0:
.string "yes\n"
.LC1:
.string "no\n"
.LC2:
.string " yes a %d\n"
.LC3:
.string "yes b %d\n"
.LC4:
.string "b > - 2\n"
.LC5:
.string "no %d\n"
.LC6:
.string "no %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $2,-16(%rbp)
movq $1,-8(%rbp)
.L1:
movq -16(%rbp),%rax
cmpq $4,%rax
jg .L2
.L3:
movq -8(%rbp),%rax
cmpq $0,%rax
jle .L4
movq -16(%rbp),%rax
cmpq $4,%rax
jg .L5
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L6
.L5:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L6:
movq -16(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-16(%rbp)
movq -8(%rbp),%rax
movq $1,%r10
subq %r10,%rax
movq %rax,-8(%rbp)
jmp .L1
.L4:
movq -16(%rbp),%rax
cmpq $4,%rax
jg .L7
movq -16(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L8
.L7:
movq -8(%rbp),%rsi
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -8(%rbp),%rax
cmpq $-2,%rax
jle .L9
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L8
.L9:
nop
.L8:
movq -16(%rbp),%rsi
leaq .LC5(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -8(%rbp),%rsi
leaq .LC6(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L6
.L2:
movq -8(%rbp),%rax
cmpq $-4,%rax
jle .L10
jmp .L3
.L10:
leave
ret

