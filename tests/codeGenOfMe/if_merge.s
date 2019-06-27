.file "tests/codeGenOfMe/if_merge.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.LC1:
.string "lu lu lu\n"
.LC2:
.string "trust\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $0,-32(%rbp)
movq $1,-24(%rbp)
movq $2,-16(%rbp)
movq $3,-8(%rbp)
movq -32(%rbp),%rax
movq -24(%rbp),%r10
cmpq %r10,%rax
jg .L1
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jle .L2
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2
.L2:
nop
jmp .L3
.L1:
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jge .L4
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L4
.L4:
nop
.L3:
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

