.file "tests/codeGenOfMe/if.dcf"
.text
.section .rodata
.LC0:
.string "a, c, d is %d, %d, %d\n"
.LC1:
.string "a, b, c, d, e, f\n"
.LC2:
.string "a, b ,c, d ,f, e\n"
.LC3:
.string "b, d, c is %d, %d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
movq $4,-48(%rbp)
movq $3,-40(%rbp)
movq $6,-32(%rbp)
movq $7,-24(%rbp)
movq $8,-16(%rbp)
movq $9,-8(%rbp)
movq -48(%rbp),%rax
movq -40(%rbp),%r10
cmpq %r10,%rax
jle .L1
movq -32(%rbp),%rax
movq -24(%rbp),%r10
cmpq %r10,%rax
jle .L2
movq -24(%rbp),%rcx
movq -32(%rbp),%rdx
movq -48(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2
.L2:
nop
jmp .L3
.L1:
movq -24(%rbp),%rax
movq -32(%rbp),%r10
cmpq %r10,%rax
jle .L4
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jle .L5
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
.L5:
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
.L4:
movq -32(%rbp),%rcx
movq -24(%rbp),%rdx
movq -40(%rbp),%rsi
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L3:
leave
ret

