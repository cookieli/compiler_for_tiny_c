.file "tests/codeGenOfMe/continue_03.dcf"
.text
.section .rodata
.LC0:
.string "a: %d\n"
.LC1:
.string "%d\n"
.LC2:
.string "break outtter\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $10,-8(%rbp)
.L1:
movq -8(%rbp),%rax
cmpq $0,%rax
jle .L2
subq $16, %rsp
movq $0,-16(%rbp)
.L3:
movq -16(%rbp),%rax
cmpq $10000,%rax
jge .L4
movq -16(%rbp),%rax
cmpq $9995,%rax
jge .L5
movq -16(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-16(%rbp)
nop
jmp .L3
jmp .L5
.L5:
movq -16(%rbp),%rax
cmpq $9999,%rax
jne .L6
nop
jmp .L4
jmp .L6
.L6:
movq -16(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -16(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-16(%rbp)
jmp .L3
.L4:
movq -8(%rbp),%rax
movq $1,%r10
subq %r10,%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rax
cmpq $5,%rax
jle .L7
nop
jmp .L1
jmp .L7
.L7:
movq -8(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -8(%rbp),%rax
cmpq $2,%rax
jne .L8
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
nop
jmp .L2
jmp .L8
.L8:
addq $16, %rsp
jmp .L1
.L2:
leave
ret

