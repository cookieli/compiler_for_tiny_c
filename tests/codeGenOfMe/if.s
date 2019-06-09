.file "tests/codeGenOfMe/if.dcf"
.text
.section .rodata
.LC0:
.string "a, c, d is %d, %d, %d\n"
.LC1:
.string "a, d, c is %d, %d, %d\n"
.LC2:
.string "b, c, d is %d, %d, %d\n"
.LC3:
.string "b, d, c is %d, %d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $4,-32(%rbp)
movq $3,-24(%rbp)
movq $6,-16(%rbp)
movq $7,-8(%rbp)
movq -32(%rbp),%rax
movq -24(%rbp),%r10
cmpq %r10,%rax
jle .L1
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jle .L2
movq -8(%rbp),%rcx
movq -16(%rbp),%rdx
movq -32(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
.L2:
movq -16(%rbp),%rcx
movq -8(%rbp),%rdx
movq -32(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L3:
jmp .L4
.L1:
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jle .L5
movq -8(%rbp),%rcx
movq -16(%rbp),%rdx
movq -24(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L6
.L5:
movq -16(%rbp),%rcx
movq -8(%rbp),%rdx
movq -24(%rbp),%rsi
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L6:
.L4:
leave
ret

