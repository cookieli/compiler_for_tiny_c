.file "tests/codeGenOfMe/if_03.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.LC1:
.string "yes the world\n"
.LC2:
.string "hello world2\n"
.LC3:
.string "hello world %d, %d\n"
.LC4:
.string "la la la %d, %d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
movq $10,-40(%rbp)
movq $30,-32(%rbp)
movq $-40,-24(%rbp)
movq $14,-16(%rbp)
movq $25,-8(%rbp)
movq -40(%rbp),%rax
cmpq $10,%rax
jg .L1
movq -32(%rbp),%rax
cmpq $40,%rax
jge .L1
.L2:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
.L1:
movq -40(%rbp),%rax
movq -32(%rbp),%r10
cmpq %r10,%rax
jge .L4
jmp .L2
.L4:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L3:
movq -40(%rbp),%rax
cmpq $10,%rax
jg .L5
movq -32(%rbp),%rax
cmpq $40,%rax
jl .L5
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L6
.L5:
movq -16(%rbp),%rax
cmpq $16,%rax
jl .L7
.L8:
movq -8(%rbp),%rdx
movq -16(%rbp),%rsi
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L6
.L7:
movq -8(%rbp),%rax
cmpq $27,%rax
jge .L9
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jl .L9
jmp .L8
.L9:
subq $32, %rsp
movq $5,-24(%rbp)
movq $7,-16(%rbp)
movq $10,-8(%rbp)
movq -8(%rbp),%rcx
movq -16(%rbp),%rdx
movq -24(%rbp),%rsi
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
addq $32, %rsp
.L6:
leave
ret

