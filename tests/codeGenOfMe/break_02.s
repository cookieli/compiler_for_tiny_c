.file "tests/codeGenOfMe/break_02.dcf"
.text
.section .rodata
.LC0:
.string "out break for i\n"
.LC1:
.string "innner break for m\n"
.LC2:
.string "inner loop\n"
.LC3:
.string "outer loop\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $0,-8(%rbp)
.L1:
movq -8(%rbp),%rax
cmpq $8,%rax
jge .L2
subq $16, %rsp
movq $8,-16(%rbp)
movq -8(%rbp),%rax
cmpq $3,%rax
jne .L3
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
nop
jmp .L2
jmp .L3
.L3:
nop
.L4:
movq -16(%rbp),%rax
cmpq $0,%rax
jle .L5
movq -16(%rbp),%rax
cmpq $4,%rax
jne .L6
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
nop
jmp .L5
jmp .L6
.L6:
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -16(%rbp),%rax
movq $1,%r10
subq %r10,%rax
movq %rax,-16(%rbp)
jmp .L4
.L5:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
addq $16, %rsp
jmp .L1
.L2:
leave
ret

