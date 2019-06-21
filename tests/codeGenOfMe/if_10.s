.file "tests/codeGenOfMe/if_10.dcf"
.text
.section .rodata
.LC0:
.string "fff\n"
.LC1:
.string "bbb\n"
.LC2:
.string "ccc\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $3,-32(%rbp)
movq $2,-24(%rbp)
movq $4,-16(%rbp)
movq $6,-8(%rbp)
movq -32(%rbp),%rax
movq -24(%rbp),%r10
cmpq %r10,%rax
jle .L1
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jge .L2
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
.L2:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L3:
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L1
.L1:
leave
ret

