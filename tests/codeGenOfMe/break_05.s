.file "tests/codeGenOfMe/break_05.dcf"
.text
.section .rodata
.LC0:
.string "outter break\n"
.LC1:
.string "inner break\n"
.LC2:
.string "another break;\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $8,-16(%rbp)
movq $0,-8(%rbp)
.L1:
movq -8(%rbp),%rax
cmpq $6,%rax
jge .L2
subq $16, %rsp
movq -8(%rbp),%rax
cmpq $3,%rax
jl .L3
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
nop
jmp .L2
jmp .L3
.L3:
movq $0,-24(%rbp)
.L4:
movq -24(%rbp),%rax
cmpq $5,%rax
jge .L5
movq -24(%rbp),%rax
cmpq $3,%rax
jne .L6
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
nop
jmp .L5
jmp .L6
.L6:
movq -24(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-24(%rbp)
jmp .L4
.L5:
movq -8(%rbp),%rax
movq $2,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
addq $16, %rsp
jmp .L1
.L2:
nop
.L7:
movq -16(%rbp),%rax
cmpq $-2,%rax
jle .L8
movq -16(%rbp),%rax
cmpq $4,%rax
jne .L9
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
nop
jmp .L8
jmp .L9
.L9:
movq -16(%rbp),%rax
movq $1,%r10
subq %r10,%rax
movq %rax,-16(%rbp)
jmp .L7
.L8:
leave
ret

